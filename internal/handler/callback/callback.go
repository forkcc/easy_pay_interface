package callback

import (
	"net/http"

	"github.com/easypay/easy_pay_interface/internal/entity"
	"github.com/easypay/easy_pay_interface/internal/repository"
	"github.com/easypay/easy_pay_interface/internal/response"
	"github.com/easypay/easy_pay_interface/internal/service"
	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
)

const statusSuccess = 1

// Routes 上游回调：支付/退款结果通知本系统，更新订单、入账、触发下游通知
func Routes(g *gin.RouterGroup, db *gorm.DB,
	payOrderRepo *repository.PayOrderRepo, refundOrderRepo *repository.RefundOrderRepo,
	merchantRepo *repository.MerchantRepo, notifyLogRepo *repository.NotifyLogRepo,
	notifySvc *service.NotifyService) {
	g.POST("/pay", payCallback(db, payOrderRepo, merchantRepo, notifyLogRepo, notifySvc))
	g.POST("/refund", refundCallback(db, refundOrderRepo, merchantRepo, notifyLogRepo, notifySvc))
}

func payCallback(db *gorm.DB, payOrderRepo *repository.PayOrderRepo, merchantRepo *repository.MerchantRepo, notifyLogRepo *repository.NotifyLogRepo, notifySvc *service.NotifyService) gin.HandlerFunc {
	return func(c *gin.Context) {
		var req struct {
			PayOrderNo  string `json:"payOrderNo" binding:"required"`
			Status      int8   `json:"status"`
			UpstreamSn  string `json:"upstreamSn"`
		}
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusOK, response.Fail(400, err.Error()))
			return
		}
		o, err := payOrderRepo.GetByPayOrderNo(req.PayOrderNo)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(404, "订单不存在"))
			return
		}
		if o.Status == statusSuccess {
			c.JSON(http.StatusOK, response.OK[any](nil))
			return
		}
		if req.Status != statusSuccess {
			o.Status = 2
			o.UpstreamSn = req.UpstreamSn
			_ = payOrderRepo.Update(o)
			c.JSON(http.StatusOK, response.OK[any](nil))
			return
		}
		o.Status = statusSuccess
		o.UpstreamSn = req.UpstreamSn
		if err := payOrderRepo.Update(o); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		if err := service.AddBalance(db, o.MerchantID, o.Amount, "pay", o.PayOrderNo); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		merchant, _ := merchantRepo.GetByID(o.MerchantID)
		if merchant != nil && merchant.NotifyURL != "" {
			body := service.BuildPayBody(o.PayOrderNo, o.Amount, o.Status, o.UpstreamSn)
			log := &entity.NotifyLog{ BizType: "pay", BizID: o.ID, NotifyURL: merchant.NotifyURL, ReqBody: body, }
			if err := notifyLogRepo.Create(log); err != nil {
				c.JSON(http.StatusOK, response.OK[any](nil))
				return
			}
			notifySvc.SendAsync(log, o.ID)
		}
		c.JSON(http.StatusOK, response.OK[any](nil))
	}
}

func refundCallback(db *gorm.DB, refundOrderRepo *repository.RefundOrderRepo, merchantRepo *repository.MerchantRepo, notifyLogRepo *repository.NotifyLogRepo, notifySvc *service.NotifyService) gin.HandlerFunc {
	return func(c *gin.Context) {
		var req struct {
			RefundOrderNo string `json:"refundOrderNo" binding:"required"`
			Status        int8   `json:"status"`
			UpstreamSn    string `json:"upstreamSn"`
		}
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusOK, response.Fail(400, err.Error()))
			return
		}
		ro, err := refundOrderRepo.GetByRefundOrderNo(req.RefundOrderNo)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(404, "退款单不存在"))
			return
		}
		if ro.Status == statusSuccess {
			c.JSON(http.StatusOK, response.OK[any](nil))
			return
		}
		if req.Status != statusSuccess {
			ro.Status = 2
			ro.UpstreamSn = req.UpstreamSn
			_ = refundOrderRepo.Update(ro)
			c.JSON(http.StatusOK, response.OK[any](nil))
			return
		}
		ro.Status = statusSuccess
		ro.UpstreamSn = req.UpstreamSn
		if err := refundOrderRepo.Update(ro); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		if err := service.AddBalance(db, ro.MerchantID, -ro.Amount, "refund", ro.RefundOrderNo); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		merchant, _ := merchantRepo.GetByID(ro.MerchantID)
		if merchant != nil && merchant.NotifyURL != "" {
			body := service.BuildRefundBody(ro.RefundOrderNo, ro.Amount, ro.Status)
			log := &entity.NotifyLog{ BizType: "refund", BizID: ro.ID, NotifyURL: merchant.NotifyURL, ReqBody: body, }
			if err := notifyLogRepo.Create(log); err != nil {
				c.JSON(http.StatusOK, response.OK[any](nil))
				return
			}
			notifySvc.SendAsync(log, 0)
		}
		c.JSON(http.StatusOK, response.OK[any](nil))
	}
}
