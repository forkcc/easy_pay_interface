package app

import (
	"time"

	"github.com/easypay/easy_pay_interface/internal/handler/agent"
	"github.com/easypay/easy_pay_interface/internal/handler/callback"
	"github.com/easypay/easy_pay_interface/internal/handler/manager"
	"github.com/easypay/easy_pay_interface/internal/handler/merchant"
	"github.com/easypay/easy_pay_interface/internal/handler/payment"
	"github.com/easypay/easy_pay_interface/internal/middleware"
	"github.com/easypay/easy_pay_interface/internal/repository"
	"github.com/easypay/easy_pay_interface/internal/service"
	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
)

func SetupRoutes(r *gin.Engine, db *gorm.DB) {
	r.Use(middleware.RateLimitGin(300, time.Minute, middleware.KeyByClientIP))
	r.Use(middleware.MetricsCollector())
	r.GET("/health", func(c *gin.Context) { c.JSON(200, gin.H{"status": "ok"}) })
	r.GET("/metrics", middleware.MetricsHandler)

	merchantRepo := repository.NewMerchantRepo(db)
	agentRepo := repository.NewAgentRepo(db)
	payOrderRepo := repository.NewPayOrderRepo(db)
	accountRepo := repository.NewAccountRepo(db)
	refundOrderRepo := repository.NewRefundOrderRepo(db)
	paymentProductRepo := repository.NewPaymentProductRepo(db)
	paymentInterfaceRepo := repository.NewPaymentInterfaceRepo(db)
	paymentChannelRepo := repository.NewPaymentChannelRepo(db)
	merchantProductRepo := repository.NewMerchantProductRepo(db)
	agentMerchantRepo := repository.NewAgentMerchantRepo(db)
	productChannelRepo := repository.NewProductChannelRepo(db)
	notifyLogRepo := repository.NewNotifyLogRepo(db)
	accountFlowRepo := repository.NewAccountFlowRepo(db)
	profitShareRuleRepo := repository.NewProfitShareRuleRepo(db)
	withdrawOrderRepo := repository.NewWithdrawOrderRepo(db)
	reconcileReportRepo := repository.NewReconcileReportRepo(db)

	adminUserRepo := repository.NewAdminUserRepo(db)
	agentUserRepo := repository.NewAgentUserRepo(db)
	auditLogRepo := repository.NewAuditLogRepo(db)

	managerGroup := r.Group("/manager")
	manager.Routes(managerGroup, merchantRepo, agentRepo, paymentProductRepo, paymentInterfaceRepo, paymentChannelRepo, merchantProductRepo, productChannelRepo, payOrderRepo, refundOrderRepo, accountRepo, accountFlowRepo, profitShareRuleRepo, withdrawOrderRepo, reconcileReportRepo, adminUserRepo, agentUserRepo, auditLogRepo, db)
	agentGroup := r.Group("/agent")
	agent.Routes(agentGroup, agentRepo, agentUserRepo, agentMerchantRepo, merchantRepo, auditLogRepo)
	merchantGroup := r.Group("/merchant")
	merchantGroup.Use(middleware.MerchantID(merchantRepo, true))
	merchant.Routes(merchantGroup, merchantRepo, payOrderRepo, accountRepo, merchantProductRepo, paymentProductRepo, withdrawOrderRepo)
	paymentGroup := r.Group("/payment")
	paymentGroup.Use(middleware.MerchantID(merchantRepo, true))
	payment.Routes(paymentGroup, payOrderRepo, accountRepo, refundOrderRepo, merchantProductRepo, paymentProductRepo, productChannelRepo, paymentChannelRepo)

	notifySvc := service.NewNotifyService(notifyLogRepo, payOrderRepo)
	callback.Routes(r.Group("/callback"), db, payOrderRepo, refundOrderRepo, merchantRepo, notifyLogRepo, notifySvc)
}
