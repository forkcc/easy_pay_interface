package bot

import (
	"context"
	"encoding/json"
	"log"
	"net/http"
	"os"
	"strconv"
	"strings"

	"github.com/easypay/easy_pay_interface/internal/entity"
	"github.com/easypay/easy_pay_interface/internal/repository"
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
	"gorm.io/gorm"
)

// Server 运行 Bot 长轮询，并提供 HTTP 接口供业务推送消息到商户群/渠道群
type Server struct {
	bot     *tgbotapi.BotAPI
	cfg     Config
	db      *gorm.DB
	bindRepo *repository.BotChatBindRepo
	merchantRepo *repository.MerchantRepo
	accountRepo  *repository.AccountRepo
	payOrderRepo *repository.PayOrderRepo
	interfaceRepo *repository.PaymentInterfaceRepo
}

func NewServer(cfg Config, db *gorm.DB) (*Server, error) {
	if cfg.Token == "" {
		return &Server{cfg: cfg, db: db}, nil
	}
	bot, err := tgbotapi.NewBotAPI(cfg.Token)
	if err != nil {
		return nil, err
	}
	bot.Debug = false
	log.Printf("Bot 已登录 @%s", bot.Self.UserName)

	bindRepo := repository.NewBotChatBindRepo(db)
	merchantRepo := repository.NewMerchantRepo(db)
	accountRepo := repository.NewAccountRepo(db)
	payOrderRepo := repository.NewPayOrderRepo(db)
	interfaceRepo := repository.NewPaymentInterfaceRepo(db)

	return &Server{
		bot:           bot,
		cfg:           cfg,
		db:            db,
		bindRepo:      bindRepo,
		merchantRepo:  merchantRepo,
		accountRepo:   accountRepo,
		payOrderRepo:  payOrderRepo,
		interfaceRepo: interfaceRepo,
	}, nil
}

// Run 启动长轮询与可选的 HTTP 推送服务
func (s *Server) Run(ctx context.Context) error {
	if s.bot == nil {
		<-ctx.Done()
		return nil
	}
	u := tgbotapi.NewUpdate(0)
	u.Timeout = 60
	updates := s.bot.GetUpdatesChan(u)

	// HTTP 推送服务（供 easy_pay 业务调用）
	mux := http.NewServeMux()
	mux.HandleFunc("/transfer/merchant", s.httpTransferMerchant)
	mux.HandleFunc("/transfer/channel", s.httpTransferChannel)
	mux.HandleFunc("/health", func(w http.ResponseWriter, _ *http.Request) { w.WriteHeader(200); _, _ = w.Write([]byte("ok")) })
	addr := ":5670"
	if a := getEnv("BOT_HTTP_ADDR", ""); a != "" {
		addr = a
	}
	svr := &http.Server{Addr: addr, Handler: mux}
	go func() {
		log.Println("Bot HTTP 推送服务监听", addr)
		_ = svr.ListenAndServe()
	}()
	defer func() { _ = svr.Shutdown(ctx) }()

	for {
		select {
		case <-ctx.Done():
			return nil
		case update := <-updates:
			s.handleUpdate(update)
		}
	}
}

func (s *Server) handleUpdate(update tgbotapi.Update) {
	if update.Message == nil || update.Message.From == nil {
		return
	}
	msg := update.Message
	text := msg.Text
	chatID := msg.Chat.ID
	userID := msg.From.ID
	isPrivate := msg.Chat.Type == "private"
	isGroup := msg.Chat.Type == "group" || msg.Chat.Type == "supergroup"

	// 私聊：仅超级用户可广播
	if isPrivate {
		if s.cfg.IsSuper(msg.From.UserName) && text != "" {
			s.broadcastToAll(text)
		}
		return
	}
	if !isGroup || text == "" {
		return
	}

	// 群内命令
	switch {
	case strings.HasPrefix(text, "绑定商户"):
		s.cmdBindMerchant(chatID, userID, strings.TrimSpace(text[len("绑定商户"):]), msg)
	case strings.HasPrefix(text, "绑定渠道"):
		s.cmdBindChannel(chatID, userID, strings.TrimSpace(text[len("绑定渠道"):]), msg)
	case text == "绑定信息" || text == "/who":
		s.cmdWho(chatID, msg)
	case text == "余额" || text == "cx" || text == "/balance":
		s.cmdBalance(chatID, msg)
	case text == "帮助" || text == "/help":
		s.cmdHelp(chatID, msg)
	default:
		// 可扩展更多命令
	}
}

func (s *Server) cmdBindMerchant(chatID int64, userID int64, arg string, msg *tgbotapi.Message) {
	if !s.cfg.IsSuper(msg.From.UserName) {
		s.reply(chatID, msg.MessageID, "仅超级用户可绑定商户群")
		return
	}
	if arg == "" {
		s.reply(chatID, msg.MessageID, "用法：绑定商户 <商户ID>\n例：绑定商户 1")
		return
	}
	merchantID, err := strconv.ParseUint(arg, 10, 32)
	if err != nil {
		s.reply(chatID, msg.MessageID, "商户ID 需为数字")
		return
	}
	m, err := s.merchantRepo.GetByID(uint(merchantID))
	if err != nil || m == nil {
		s.reply(chatID, msg.MessageID, "商户不存在")
		return
	}
	exist, _ := s.bindRepo.GetByChatID(chatID)
	if exist != nil {
		exist.BindType = entity.BotBindTypeMerchant
		exist.BindID = strconv.FormatUint(merchantID, 10)
		_ = s.bindRepo.Save(exist)
	} else {
		_ = s.bindRepo.Create(&entity.BotChatBind{
			ChatID:   chatID,
			BindType: entity.BotBindTypeMerchant,
			BindID:   strconv.FormatUint(merchantID, 10),
		})
	}
	s.reply(chatID, msg.MessageID, "已绑定商户群\n商户名: "+m.Name+"\n商户ID: "+arg)
}

func (s *Server) cmdBindChannel(chatID int64, userID int64, arg string, msg *tgbotapi.Message) {
	if !s.cfg.IsSuper(msg.From.UserName) {
		s.reply(chatID, msg.MessageID, "仅超级用户可绑定渠道群")
		return
	}
	if arg == "" {
		s.reply(chatID, msg.MessageID, "用法：绑定渠道 <接口编码>\n例：绑定渠道 WXPAY")
		return
	}
	iface, err := s.interfaceRepo.GetByInterfaceCode(arg)
	if err != nil || iface == nil {
		s.reply(chatID, msg.MessageID, "支付接口不存在")
		return
	}
	exist, _ := s.bindRepo.GetByChatID(chatID)
	if exist != nil {
		exist.BindType = entity.BotBindTypeChannel
		exist.BindID = arg
		_ = s.bindRepo.Save(exist)
	} else {
		_ = s.bindRepo.Create(&entity.BotChatBind{
			ChatID:   chatID,
			BindType: entity.BotBindTypeChannel,
			BindID:   arg,
		})
	}
	s.reply(chatID, msg.MessageID, "已绑定渠道群\n接口: "+iface.Name+"\n编码: "+arg)
}

func (s *Server) cmdWho(chatID int64, msg *tgbotapi.Message) {
	bind, err := s.bindRepo.GetByChatID(chatID)
	if err != nil || bind == nil {
		s.reply(chatID, msg.MessageID, "本群未绑定，请使用「绑定商户 <ID>」或「绑定渠道 <编码>」")
		return
	}
	if bind.BindType == entity.BotBindTypeMerchant {
		var m *entity.Merchant
		if id, e := strconv.ParseUint(bind.BindID, 10, 32); e == nil {
			m, _ = s.merchantRepo.GetByID(uint(id))
		}
		if m != nil {
			s.reply(chatID, msg.MessageID, "商户群\n商户名: "+m.Name+"\n商户号: "+m.MerchantNo+"\n商户ID: "+bind.BindID)
			return
		}
	}
	if bind.BindType == entity.BotBindTypeChannel {
		iface, _ := s.interfaceRepo.GetByInterfaceCode(bind.BindID)
		if iface != nil {
			s.reply(chatID, msg.MessageID, "渠道群\n接口: "+iface.Name+"\n编码: "+bind.BindID)
			return
		}
	}
	s.reply(chatID, msg.MessageID, "绑定信息异常")
}

func (s *Server) cmdBalance(chatID int64, msg *tgbotapi.Message) {
	bind, err := s.bindRepo.GetByChatID(chatID)
	if err != nil || bind == nil {
		return
	}
	if bind.BindType == entity.BotBindTypeMerchant {
		merchantID, _ := strconv.ParseUint(bind.BindID, 10, 32)
		acc, err := s.accountRepo.GetByMerchantID(uint(merchantID))
		if err != nil || acc == nil {
			s.reply(chatID, msg.MessageID, "账户不存在或余额为 0")
			return
		}
		balance := (acc.Balance - acc.Frozen) / 100
		frozen := acc.Frozen / 100
		s.reply(chatID, msg.MessageID, "账户余额: "+strconv.FormatInt(balance, 10)+" 元\n冻结: "+strconv.FormatInt(frozen, 10)+" 元")
		return
	}
	if bind.BindType == entity.BotBindTypeChannel {
		// 渠道群：可展示今日该接口下支付成功笔数/金额（需按 channel 统计，这里简化为提示）
		s.reply(chatID, msg.MessageID, "渠道群无余额，请使用管理端查看统计数据")
	}
}

func (s *Server) cmdHelp(chatID int64, msg *tgbotapi.Message) {
	help := `命令说明：
• 绑定商户 <商户ID> — 将本群设为该商户的商户群（仅超级用户）
• 绑定渠道 <接口编码> — 将本群设为该支付接口的渠道群（仅超级用户）
• 绑定信息 /who — 查看本群当前绑定
• 余额 /cx /balance — 查看余额（商户群）
• 帮助 /help — 本说明`
	s.reply(chatID, msg.MessageID, help)
}

func (s *Server) broadcastToAll(text string) {
	if s.bot == nil {
		return
	}
	chatIDs, err := s.bindRepo.ListAllChatIDs()
	if err != nil || len(chatIDs) == 0 {
		return
	}
	for _, cid := range chatIDs {
		msg := tgbotapi.NewMessage(cid, text)
		_, _ = s.bot.Send(msg)
	}
}

func (s *Server) reply(chatID int64, replyToID int, text string) {
	if s.bot == nil {
		return
	}
	msg := tgbotapi.NewMessage(chatID, text)
	msg.ReplyToMessageID = replyToID
	_, _ = s.bot.Send(msg)
}

// SendToMerchant 向商户群推送消息（供 HTTP 或业务调用）
func (s *Server) SendToMerchant(merchantID uint, text string) bool {
	if s.bot == nil {
		return false
	}
	chatID, err := s.bindRepo.GetChatIDByMerchantID(merchantID)
	if err != nil {
		return false
	}
	msg := tgbotapi.NewMessage(chatID, text)
	_, err = s.bot.Send(msg)
	return err == nil
}

// SendToChannel 向渠道群推送消息
func (s *Server) SendToChannel(interfaceCode string, text string) bool {
	if s.bot == nil {
		return false
	}
	chatID, err := s.bindRepo.GetChatIDByChannel(interfaceCode)
	if err != nil {
		return false
	}
	msg := tgbotapi.NewMessage(chatID, text)
	_, err = s.bot.Send(msg)
	return err == nil
}

func getEnv(key, def string) string {
	if v := os.Getenv(key); v != "" {
		return v
	}
	return def
}

// HTTP: POST /transfer/merchant  Body: {"merchantId":1,"message":"xxx"}
func (s *Server) httpTransferMerchant(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		w.WriteHeader(http.StatusMethodNotAllowed)
		return
	}
	var req struct {
		MerchantID uint   `json:"merchantId"`
		Message    string `json:"message"`
	}
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		w.WriteHeader(http.StatusBadRequest)
		return
	}
	if req.MerchantID == 0 || req.Message == "" {
		w.WriteHeader(http.StatusBadRequest)
		w.Write([]byte("merchantId 与 message 必填"))
		return
	}
	if s.SendToMerchant(req.MerchantID, req.Message) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("ok"))
	} else {
		w.WriteHeader(http.StatusNotFound)
		w.Write([]byte("未绑定商户群或发送失败"))
	}
}

// HTTP: POST /transfer/channel  Body: {"interfaceCode":"WXPAY","message":"xxx"}
func (s *Server) httpTransferChannel(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		w.WriteHeader(http.StatusMethodNotAllowed)
		return
	}
	var req struct {
		InterfaceCode string `json:"interfaceCode"`
		Message       string `json:"message"`
	}
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		w.WriteHeader(http.StatusBadRequest)
		return
	}
	if req.InterfaceCode == "" || req.Message == "" {
		w.WriteHeader(http.StatusBadRequest)
		w.Write([]byte("interfaceCode 与 message 必填"))
		return
	}
	if s.SendToChannel(req.InterfaceCode, req.Message) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("ok"))
	} else {
		w.WriteHeader(http.StatusNotFound)
		w.Write([]byte("未绑定渠道群或发送失败"))
	}
}
