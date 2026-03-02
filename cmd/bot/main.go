// bot 模块：Telegram Bot，独立运行。
package main

import (
	"log"

	"github.com/easypay/easy_pay_interface/internal/config"
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
)

func main() {
	cfg := config.Load()
	if cfg.TelegramBotToken == "" {
		log.Fatal("bot: TELEGRAM_BOT_TOKEN is required")
	}
	bot, err := tgbotapi.NewBotAPI(cfg.TelegramBotToken)
	if err != nil {
		log.Fatal("bot: ", err)
	}
	bot.Debug = false
	log.Printf("bot: started as @%s (scaffold)", bot.Self.UserName)

	u := tgbotapi.NewUpdate(0)
	u.Timeout = 60
	updates := bot.GetUpdatesChan(u)
	for update := range updates {
		if update.Message != nil {
			// TODO: 处理命令与回调
			_, _ = bot.Send(tgbotapi.NewMessage(update.Message.Chat.ID, "ok"))
		}
	}
}
