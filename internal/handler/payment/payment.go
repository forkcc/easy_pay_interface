package payment

import (
	"net/http"
	"strconv"
	"time"

	"github.com/easypay/easy_pay_interface/internal/entity"
	"github.com/easypay/easy_pay_interface/internal/middleware"
	"github.com/easypay/easy_pay_interface/internal/repository"
	"github.com/easypay/easy_pay_interface/internal/response"
	"github.com/gin-gonic/gin"
)

func Routes(g *gin.RouterGroup, payOrderRepo *repository.PayOrderRepo, accountRepo *repository.AccountRepo, refundOrderRepo *repository.RefundOrderRepo,
	merchantProductRepo *repository.MerchantProductRepo, paymentProductRepo *repository.PaymentProductRepo, productChannelRepo *repository.ProductChannelRepo, paymentChannelRepo *repository.PaymentChannelRepo) {
	g.GET("/health", func(c *gin.Context) { c.JSON(http.StatusOK, response.OK("ok")) })
	g.GET("/order/query", orderQuery(payOrderRepo))
	g.POST("/order/create", orderCreate(payOrderRepo, merchantProductRepo, paymentProductRepo, productChannelRepo, paymentChannelRepo))
	g.GET("/balance", balance(accountRepo))
	g.POST("/refund/apply", refundApply(payOrderRepo, refundOrderRepo))
	g.GET("/refund/query", refundQuery(refundOrderRepo))
}

const defaultMerchantID = 1 // 鉴权未命中时中间件默认值

func orderQuery(repo *repository.PayOrderRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		no := c.Query("payOrderNo")
		if no == "" {
			c.JSON(http.StatusOK, response.Fail(400, "payOrderNo 必填"))
			return
		}
		o, err := repo.GetByPayOrderNo(no)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(404, "订单不存在"))
			return
		}
		c.JSON(http.StatusOK, response.OK(gin.H{
			"payOrderNo": o.PayOrderNo,
			"amount":     o.Amount,
			"status":     o.Status,
			"upstreamSn": o.UpstreamSn,
			"createdAt":  o.CreatedAt.Format(time.RFC3339),
		}))
	}
}

func orderCreate(payOrderRepo *repository.PayOrderRepo,
	merchantProductRepo *repository.MerchantProductRepo, paymentProductRepo *repository.PaymentProductRepo,
	productChannelRepo *repository.ProductChannelRepo, paymentChannelRepo *repository.PaymentChannelRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		var req struct {
			OutTradeNo  string `json:"outTradeNo" binding:"required"`
			ProductCode string `json:"productCode" binding:"required"`
			Amount      int64  `json:"amount" binding:"required,gt=0"`
		}
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusOK, response.Fail(400, err.Error()))
			return
		}
		merchantID := middleware.GetMerchantID(c)
		// 幂等：同一商户同一商户订单号返回已有订单
		exist, err := payOrderRepo.GetByMerchantIDAndOutTradeNo(merchantID, req.OutTradeNo)
		if err == nil {
			c.JSON(http.StatusOK, response.OK(gin.H{
				"payOrderNo": exist.PayOrderNo,
				"amount":     exist.Amount,
				"status":     exist.Status,
				"createdAt":  exist.CreatedAt.Format(time.RFC3339),
			}))
			return
		}
		// 按产品选渠道：1) 产品存在且启用 2) 商户已配置该产品 3) 产品下已配置渠道，取第一个启用渠道
		product, err := paymentProductRepo.GetByProductCode(req.ProductCode)
		if err != nil || product.Status != 1 {
			c.JSON(http.StatusOK, response.Fail(400, "产品不存在或未启用"))
			return
		}
		merchantProductIDs, err := merchantProductRepo.ListProductIDsByMerchantID(merchantID)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		hasProduct := false
		for _, pid := range merchantProductIDs {
			if pid == product.ID {
				hasProduct = true
				break
			}
		}
		if !hasProduct {
			c.JSON(http.StatusOK, response.Fail(400, "商户未开通该支付产品"))
			return
		}
		productChannels, err := productChannelRepo.ListByProductID(product.ID)
		if err != nil || len(productChannels) == 0 {
			c.JSON(http.StatusOK, response.Fail(400, "该产品暂无可用渠道"))
			return
		}
		var channelID uint
		for _, pc := range productChannels {
			ch, err := paymentChannelRepo.GetByID(pc.ChannelID)
			if err != nil || ch.Status != 1 {
				continue
			}
			channelID = ch.ID
			break
		}
		if channelID == 0 {
			c.JSON(http.StatusOK, response.Fail(400, "该产品暂无可用渠道"))
			return
		}
		payOrderNo := "P" + strconv.FormatInt(time.Now().UnixMilli(), 10) + strconv.Itoa(int(time.Now().UnixNano()%10000))
		o := &entity.PayOrder{
			PayOrderNo:   payOrderNo,
			OutTradeNo:   req.OutTradeNo,
			MerchantID:   merchantID,
			ChannelID:    channelID,
			Amount:       req.Amount,
			Status:       0,
			NotifyStatus: 0,
		}
		if err := payOrderRepo.Create(o); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK(gin.H{
			"payOrderNo": o.PayOrderNo,
			"amount":     o.Amount,
			"status":     o.Status,
			"createdAt":  o.CreatedAt.Format(time.RFC3339),
		}))
	}
}

func balance(repo *repository.AccountRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		merchantID := middleware.GetMerchantID(c)
		a, err := repo.GetByMerchantID(merchantID)
		if err != nil {
			c.JSON(http.StatusOK, response.OK(gin.H{"balance": int64(0), "frozen": int64(0)}))
			return
		}
		c.JSON(http.StatusOK, response.OK(gin.H{
			"balance": a.Balance - a.Frozen,
			"frozen":  a.Frozen,
		}))
	}
}

func refundApply(payOrderRepo *repository.PayOrderRepo, refundOrderRepo *repository.RefundOrderRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		var req struct {
			PayOrderNo   string `json:"payOrderNo" binding:"required"`
			OutRefundNo  string `json:"outRefundNo" binding:"required"`
			Amount       int64  `json:"amount" binding:"required,gt=0"`
		}
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusOK, response.Fail(400, err.Error()))
			return
		}
		payOrder, err := payOrderRepo.GetByPayOrderNo(req.PayOrderNo)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(404, "支付订单不存在"))
			return
		}
		if req.Amount > payOrder.Amount {
			c.JSON(http.StatusOK, response.Fail(400, "退款金额不能大于支付金额"))
			return
		}
		// 幂等：同一商户同一商户退款单号返回已有退款单
		exist, err := refundOrderRepo.GetByMerchantIDAndOutRefundNo(payOrder.MerchantID, req.OutRefundNo)
		if err == nil {
			c.JSON(http.StatusOK, response.OK(gin.H{
				"refundOrderNo": exist.RefundOrderNo,
				"amount":        exist.Amount,
				"status":       exist.Status,
				"createdAt":    exist.CreatedAt.Format(time.RFC3339),
			}))
			return
		}
		refundOrderNo := "R" + strconv.FormatInt(time.Now().UnixMilli(), 10) + strconv.Itoa(int(time.Now().UnixNano()%10000))
		ro := &entity.RefundOrder{
			RefundOrderNo: refundOrderNo,
			OutRefundNo:   req.OutRefundNo,
			PayOrderID:    payOrder.ID,
			MerchantID:    payOrder.MerchantID,
			Amount:        req.Amount,
			Status:        0,
		}
		if err := refundOrderRepo.Create(ro); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK(gin.H{
			"refundOrderNo": ro.RefundOrderNo,
			"amount":        ro.Amount,
			"status":       ro.Status,
			"createdAt":    ro.CreatedAt.Format(time.RFC3339),
		}))
	}
}

func refundQuery(repo *repository.RefundOrderRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		no := c.Query("refundOrderNo")
		if no == "" {
			c.JSON(http.StatusOK, response.Fail(400, "refundOrderNo 必填"))
			return
		}
		ro, err := repo.GetByRefundOrderNo(no)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(404, "退款单不存在"))
			return
		}
		c.JSON(http.StatusOK, response.OK(gin.H{
			"refundOrderNo": ro.RefundOrderNo,
			"amount":        ro.Amount,
			"status":       ro.Status,
			"createdAt":    ro.CreatedAt.Format(time.RFC3339),
		}))
	}
}
