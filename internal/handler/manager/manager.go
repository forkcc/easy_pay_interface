package manager

import (
	"net/http"
	"strconv"
	"time"

	"github.com/easypay/easy_pay_interface/internal/audit"
	"github.com/easypay/easy_pay_interface/internal/entity"
	"github.com/easypay/easy_pay_interface/internal/middleware"
	"github.com/easypay/easy_pay_interface/internal/response"
	"github.com/easypay/easy_pay_interface/internal/repository"
	"github.com/easypay/easy_pay_interface/internal/service"
	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
)

func Routes(g *gin.RouterGroup, merchantRepo *repository.MerchantRepo, agentRepo *repository.AgentRepo,
	paymentProductRepo *repository.PaymentProductRepo, paymentInterfaceRepo *repository.PaymentInterfaceRepo, paymentChannelRepo *repository.PaymentChannelRepo,
	merchantProductRepo *repository.MerchantProductRepo, productChannelRepo *repository.ProductChannelRepo,
	payOrderRepo *repository.PayOrderRepo, refundOrderRepo *repository.RefundOrderRepo, accountRepo *repository.AccountRepo, accountFlowRepo *repository.AccountFlowRepo,
	profitShareRuleRepo *repository.ProfitShareRuleRepo, withdrawOrderRepo *repository.WithdrawOrderRepo, reconcileReportRepo *repository.ReconcileReportRepo,
	adminUserRepo *repository.AdminUserRepo, agentUserRepo *repository.AgentUserRepo, auditLogRepo *repository.AuditLogRepo, db *gorm.DB) {
	g.GET("/health", func(c *gin.Context) { c.JSON(http.StatusOK, response.OK("ok")) })
	g.POST("/login", managerLogin(adminUserRepo))
	g.POST("/init", managerInit(adminUserRepo))
	g.Use(middleware.AdminAuth())
	g.GET("/audit/page", auditPage(auditLogRepo))
	g.GET("/merchant/page", merchantPage(merchantRepo))
	g.GET("/merchant/:id", merchantGet(merchantRepo))
	g.POST("/merchant", merchantCreate(merchantRepo, auditLogRepo))
	g.PUT("/merchant/:id", merchantUpdate(merchantRepo, auditLogRepo))
	g.GET("/merchant/:id/products", merchantProducts(merchantProductRepo, paymentProductRepo))
	g.POST("/merchant/:id/product", merchantProductAdd(merchantProductRepo, merchantRepo, paymentProductRepo))
	g.DELETE("/merchant/:id/product/:productId", merchantProductRemove(merchantProductRepo))
	g.GET("/reconcile/export", reconcileExport(payOrderRepo, refundOrderRepo, accountRepo, accountFlowRepo))
	g.GET("/reconcile/reports", reconcileReports(reconcileReportRepo))
	g.GET("/profit/page", profitSharePage(profitShareRuleRepo))
	g.GET("/profit/:id", profitShareGet(profitShareRuleRepo))
	g.POST("/profit", profitShareCreate(profitShareRuleRepo))
	g.PUT("/profit/:id", profitShareUpdate(profitShareRuleRepo))
	g.DELETE("/profit/:id", profitShareDelete(profitShareRuleRepo))
	g.GET("/withdraw/page", withdrawPage(withdrawOrderRepo))
	g.GET("/withdraw/:id", withdrawGet(withdrawOrderRepo))
	g.PUT("/withdraw/:id/audit", withdrawAudit(db, withdrawOrderRepo, accountRepo, auditLogRepo))
	g.GET("/agent/page", agentPage(agentRepo))
	g.GET("/agent/:id", agentGet(agentRepo))
	g.POST("/agent", agentCreate(agentRepo, auditLogRepo))
	g.PUT("/agent/:id", agentUpdate(agentRepo, auditLogRepo))
	g.POST("/agent/:id/user", agentUserCreate(agentRepo, agentUserRepo))
	g.GET("/product/page", productPage(paymentProductRepo))
	g.GET("/product/:id", productGet(paymentProductRepo))
	g.POST("/product", productCreate(paymentProductRepo))
	g.PUT("/product/:id", productUpdate(paymentProductRepo))
	g.GET("/product/:id/channels", productChannels(productChannelRepo, paymentChannelRepo))
	g.POST("/product/:id/channel", productChannelAdd(productChannelRepo, paymentProductRepo, paymentChannelRepo))
	g.PUT("/product/:id/channel/:channelId", productChannelSort(productChannelRepo))
	g.DELETE("/product/:id/channel/:channelId", productChannelRemove(productChannelRepo))
	g.GET("/interface/page", interfacePage(paymentInterfaceRepo))
	g.GET("/interface/:id", interfaceGet(paymentInterfaceRepo))
	g.POST("/interface", interfaceCreate(paymentInterfaceRepo))
	g.PUT("/interface/:id", interfaceUpdate(paymentInterfaceRepo))
	g.GET("/channel/page", channelPage(paymentChannelRepo))
	g.GET("/channel/:id", channelGet(paymentChannelRepo))
	g.POST("/channel", channelCreate(paymentChannelRepo))
	g.PUT("/channel/:id", channelUpdate(paymentChannelRepo))
}

func auditPage(repo *repository.AuditLogRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		page, _ := strconv.ParseInt(c.DefaultQuery("current", "1"), 10, 64)
		size, _ := strconv.ParseInt(c.DefaultQuery("size", "20"), 10, 64)
		userType := c.Query("userType")
		action := c.Query("action")
		var startTime, endTime *time.Time
		if s := c.Query("startTime"); s != "" {
			t, err := time.Parse("2006-01-02", s)
			if err == nil {
				startTime = &t
			}
		}
		if s := c.Query("endTime"); s != "" {
			t, err := time.Parse("2006-01-02", s)
			if err == nil {
				end := t.Add(24*time.Hour - time.Nanosecond)
				endTime = &end
			}
		}
		list, total, err := repo.List(page, size, userType, action, startTime, endTime)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK(response.PageResult[entity.AuditLog]{
			Current: page, Size: size, Total: total, Records: list,
		}))
	}
}

func merchantPage(repo *repository.MerchantRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		page, _ := strconv.ParseInt(c.DefaultQuery("current", "1"), 10, 64)
		size, _ := strconv.ParseInt(c.DefaultQuery("size", "20"), 10, 64)
		name := c.Query("name")
		var status *int8
		if s := c.Query("status"); s != "" {
			v, _ := strconv.ParseInt(s, 10, 8)
			t := int8(v)
			status = &t
		}
		list, total, err := repo.List(page, size, name, status)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK(response.PageResult[entity.Merchant]{
			Current: page, Size: size, Total: total, Records: list,
		}))
	}
}

func merchantGet(repo *repository.MerchantRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		id, _ := strconv.ParseUint(c.Param("id"), 10, 32)
		m, err := repo.GetByID(uint(id))
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(404, "商户不存在"))
			return
		}
		c.JSON(http.StatusOK, response.OK(m))
	}
}

func merchantCreate(repo *repository.MerchantRepo, auditLogRepo *repository.AuditLogRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		var req struct {
			MerchantNo string `json:"merchantNo" binding:"required"`
			Name       string `json:"name" binding:"required"`
			Status     *int8  `json:"status"`
			NotifyURL  string `json:"notifyUrl"`
			ApiKey     string `json:"apiKey"`
		}
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusOK, response.Fail(400, err.Error()))
			return
		}
		_, err := repo.GetByMerchantNo(req.MerchantNo)
		if err == nil {
			c.JSON(http.StatusOK, response.Fail(400, "商户号已存在"))
			return
		}
		if req.ApiKey != "" {
			if _, err := repo.GetByApiKey(req.ApiKey); err == nil {
				c.JSON(http.StatusOK, response.Fail(400, "apiKey 已被使用"))
				return
			}
		}
		status := int8(1)
		if req.Status != nil {
			status = *req.Status
		}
		m := &entity.Merchant{MerchantNo: req.MerchantNo, Name: req.Name, Status: status, NotifyURL: req.NotifyURL, ApiKey: req.ApiKey}
		if err := repo.Create(m); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		audit.LogAdmin(c, auditLogRepo, "merchant_create", "merchant", strconv.FormatUint(uint64(m.ID), 10), gin.H{"merchantNo": m.MerchantNo, "name": m.Name})
		c.JSON(http.StatusOK, response.OK(m.ID))
	}
}

func merchantUpdate(repo *repository.MerchantRepo, auditLogRepo *repository.AuditLogRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		id, _ := strconv.ParseUint(c.Param("id"), 10, 32)
		m, err := repo.GetByID(uint(id))
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(404, "商户不存在"))
			return
		}
		var req struct {
			MerchantNo string `json:"merchantNo"`
			Name       string `json:"name"`
			Status     *int8  `json:"status"`
			NotifyURL  string `json:"notifyUrl"`
			ApiKey     string `json:"apiKey"`
		}
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusOK, response.Fail(400, err.Error()))
			return
		}
		if req.MerchantNo != "" {
			m.MerchantNo = req.MerchantNo
		}
		if req.Name != "" {
			m.Name = req.Name
		}
		if req.Status != nil {
			m.Status = *req.Status
		}
		m.NotifyURL = req.NotifyURL
		if req.ApiKey != "" {
			exist, _ := repo.GetByApiKey(req.ApiKey)
			if exist != nil && exist.ID != m.ID {
				c.JSON(http.StatusOK, response.Fail(400, "apiKey 已被其他商户使用"))
				return
			}
			m.ApiKey = req.ApiKey
		}
		if err := repo.Update(m); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		audit.LogAdmin(c, auditLogRepo, "merchant_update", "merchant", c.Param("id"), gin.H{"name": m.Name})
		c.JSON(http.StatusOK, response.OK[any](nil))
	}
}

func agentPage(repo *repository.AgentRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		page, _ := strconv.ParseInt(c.DefaultQuery("current", "1"), 10, 64)
		size, _ := strconv.ParseInt(c.DefaultQuery("size", "20"), 10, 64)
		list, total, err := repo.List(page, size)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK(response.PageResult[entity.Agent]{
			Current: page, Size: size, Total: total, Records: list,
		}))
	}
}

func agentGet(repo *repository.AgentRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		id, _ := strconv.ParseUint(c.Param("id"), 10, 32)
		a, err := repo.GetByID(uint(id))
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(404, "代理不存在"))
			return
		}
		c.JSON(http.StatusOK, response.OK(a))
	}
}

func agentCreate(repo *repository.AgentRepo, auditLogRepo *repository.AuditLogRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		var req struct {
			AgentNo  string `json:"agentNo" binding:"required"`
			Name     string `json:"name" binding:"required"`
			ParentID *uint  `json:"parentId"`
			Status   *int8  `json:"status"`
			ApiKey   string `json:"apiKey"`
		}
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusOK, response.Fail(400, err.Error()))
			return
		}
		if _, err := repo.GetByAgentNo(req.AgentNo); err == nil {
			c.JSON(http.StatusOK, response.Fail(400, "代理号已存在"))
			return
		}
		if req.ApiKey != "" {
			if _, err := repo.GetByApiKey(req.ApiKey); err == nil {
				c.JSON(http.StatusOK, response.Fail(400, "apiKey 已被使用"))
				return
			}
		}
		status := int8(1)
		if req.Status != nil {
			status = *req.Status
		}
		a := &entity.Agent{AgentNo: req.AgentNo, Name: req.Name, ParentID: req.ParentID, Status: status, ApiKey: req.ApiKey}
		if err := repo.Create(a); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		audit.LogAdmin(c, auditLogRepo, "agent_create", "agent", strconv.FormatUint(uint64(a.ID), 10), gin.H{"agentNo": a.AgentNo, "name": a.Name})
		c.JSON(http.StatusOK, response.OK(a.ID))
	}
}

func agentUpdate(repo *repository.AgentRepo, auditLogRepo *repository.AuditLogRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		id, _ := strconv.ParseUint(c.Param("id"), 10, 32)
		a, err := repo.GetByID(uint(id))
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(404, "代理不存在"))
			return
		}
		var req struct {
			AgentNo  string `json:"agentNo"`
			Name     string `json:"name"`
			ParentID *uint  `json:"parentId"`
			Status   *int8  `json:"status"`
			ApiKey   string `json:"apiKey"`
		}
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusOK, response.Fail(400, err.Error()))
			return
		}
		if req.AgentNo != "" {
			a.AgentNo = req.AgentNo
		}
		if req.Name != "" {
			a.Name = req.Name
		}
		if req.ParentID != nil {
			a.ParentID = req.ParentID
		}
		if req.Status != nil {
			a.Status = *req.Status
		}
		if req.ApiKey != "" {
			exist, _ := repo.GetByApiKey(req.ApiKey)
			if exist != nil && exist.ID != a.ID {
				c.JSON(http.StatusOK, response.Fail(400, "apiKey 已被其他代理使用"))
				return
			}
			a.ApiKey = req.ApiKey
		}
		if err := repo.Update(a); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		audit.LogAdmin(c, auditLogRepo, "agent_update", "agent", c.Param("id"), gin.H{"name": a.Name})
		c.JSON(http.StatusOK, response.OK[any](nil))
	}
}

func agentUserCreate(agentRepo *repository.AgentRepo, agentUserRepo *repository.AgentUserRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		agentID, _ := strconv.ParseUint(c.Param("id"), 10, 32)
		_, err := agentRepo.GetByID(uint(agentID))
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(404, "代理不存在"))
			return
		}
		var req struct {
			Username string `json:"username" binding:"required"`
			Password string `json:"password" binding:"required"`
			Name     string `json:"name"`
		}
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusOK, response.Fail(400, "参数错误"))
			return
		}
		_, err = agentUserRepo.GetByAgentIDAndUsername(uint(agentID), req.Username)
		if err == nil {
			c.JSON(http.StatusOK, response.Fail(400, "该代理下用户名已存在"))
			return
		}
		hash, err := service.HashPassword(req.Password)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(500, "加密失败"))
			return
		}
		u := &entity.AgentUser{AgentID: uint(agentID), Username: req.Username, PasswordHash: hash, Name: req.Name, Status: 1}
		if err := agentUserRepo.Create(u); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK(gin.H{"id": u.ID, "username": u.Username}))
	}
}

func productPage(repo *repository.PaymentProductRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		page, _ := strconv.ParseInt(c.DefaultQuery("current", "1"), 10, 64)
		size, _ := strconv.ParseInt(c.DefaultQuery("size", "20"), 10, 64)
		name := c.Query("name")
		var status *int8
		if s := c.Query("status"); s != "" {
			v, _ := strconv.ParseInt(s, 10, 8)
			t := int8(v)
			status = &t
		}
		list, total, err := repo.List(page, size, name, status)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK(response.PageResult[entity.PaymentProduct]{
			Current: page, Size: size, Total: total, Records: list,
		}))
	}
}

func productGet(repo *repository.PaymentProductRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		id, _ := strconv.ParseUint(c.Param("id"), 10, 32)
		p, err := repo.GetByID(uint(id))
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(404, "支付产品不存在"))
			return
		}
		c.JSON(http.StatusOK, response.OK(p))
	}
}

func productCreate(repo *repository.PaymentProductRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		var req struct {
			ProductCode string `json:"productCode" binding:"required"`
			Name        string `json:"name" binding:"required"`
			Status      *int8  `json:"status"`
		}
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusOK, response.Fail(400, err.Error()))
			return
		}
		if _, err := repo.GetByProductCode(req.ProductCode); err == nil {
			c.JSON(http.StatusOK, response.Fail(400, "产品编码已存在"))
			return
		}
		status := int8(1)
		if req.Status != nil {
			status = *req.Status
		}
		p := &entity.PaymentProduct{ProductCode: req.ProductCode, Name: req.Name, Status: status}
		if err := repo.Create(p); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK(p.ID))
	}
}

func productUpdate(repo *repository.PaymentProductRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		id, _ := strconv.ParseUint(c.Param("id"), 10, 32)
		p, err := repo.GetByID(uint(id))
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(404, "支付产品不存在"))
			return
		}
		var req struct {
			ProductCode string `json:"productCode"`
			Name        string `json:"name"`
			Status      *int8  `json:"status"`
		}
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusOK, response.Fail(400, err.Error()))
			return
		}
		if req.ProductCode != "" {
			p.ProductCode = req.ProductCode
		}
		if req.Name != "" {
			p.Name = req.Name
		}
		if req.Status != nil {
			p.Status = *req.Status
		}
		if err := repo.Update(p); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK[any](nil))
	}
}

func interfacePage(repo *repository.PaymentInterfaceRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		page, _ := strconv.ParseInt(c.DefaultQuery("current", "1"), 10, 64)
		size, _ := strconv.ParseInt(c.DefaultQuery("size", "20"), 10, 64)
		name := c.Query("name")
		var status *int8
		if s := c.Query("status"); s != "" {
			v, _ := strconv.ParseInt(s, 10, 8)
			t := int8(v)
			status = &t
		}
		list, total, err := repo.List(page, size, name, status)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK(response.PageResult[entity.PaymentInterface]{
			Current: page, Size: size, Total: total, Records: list,
		}))
	}
}

func interfaceGet(repo *repository.PaymentInterfaceRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		id, _ := strconv.ParseUint(c.Param("id"), 10, 32)
		p, err := repo.GetByID(uint(id))
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(404, "支付接口不存在"))
			return
		}
		c.JSON(http.StatusOK, response.OK(p))
	}
}

func interfaceCreate(repo *repository.PaymentInterfaceRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		var req struct {
			InterfaceCode string `json:"interfaceCode" binding:"required"`
			Name          string `json:"name" binding:"required"`
			RequestTpl    string `json:"requestTpl"`
			Status        *int8  `json:"status"`
		}
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusOK, response.Fail(400, err.Error()))
			return
		}
		if _, err := repo.GetByInterfaceCode(req.InterfaceCode); err == nil {
			c.JSON(http.StatusOK, response.Fail(400, "接口编码已存在"))
			return
		}
		status := int8(1)
		if req.Status != nil {
			status = *req.Status
		}
		p := &entity.PaymentInterface{InterfaceCode: req.InterfaceCode, Name: req.Name, RequestTpl: req.RequestTpl, Status: status}
		if err := repo.Create(p); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK(p.ID))
	}
}

func interfaceUpdate(repo *repository.PaymentInterfaceRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		id, _ := strconv.ParseUint(c.Param("id"), 10, 32)
		p, err := repo.GetByID(uint(id))
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(404, "支付接口不存在"))
			return
		}
		var req struct {
			InterfaceCode string `json:"interfaceCode"`
			Name          string `json:"name"`
			RequestTpl    string `json:"requestTpl"`
			Status        *int8  `json:"status"`
		}
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusOK, response.Fail(400, err.Error()))
			return
		}
		if req.InterfaceCode != "" {
			p.InterfaceCode = req.InterfaceCode
		}
		if req.Name != "" {
			p.Name = req.Name
		}
		if req.RequestTpl != "" {
			p.RequestTpl = req.RequestTpl
		}
		if req.Status != nil {
			p.Status = *req.Status
		}
		if err := repo.Update(p); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK[any](nil))
	}
}

func channelPage(repo *repository.PaymentChannelRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		page, _ := strconv.ParseInt(c.DefaultQuery("current", "1"), 10, 64)
		size, _ := strconv.ParseInt(c.DefaultQuery("size", "20"), 10, 64)
		var interfaceID *uint
		if s := c.Query("interfaceId"); s != "" {
			v, _ := strconv.ParseUint(s, 10, 32)
			u := uint(v)
			interfaceID = &u
		}
		var status *int8
		if s := c.Query("status"); s != "" {
			v, _ := strconv.ParseInt(s, 10, 8)
			t := int8(v)
			status = &t
		}
		list, total, err := repo.List(page, size, interfaceID, status)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK(response.PageResult[entity.PaymentChannel]{
			Current: page, Size: size, Total: total, Records: list,
		}))
	}
}

func channelGet(repo *repository.PaymentChannelRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		id, _ := strconv.ParseUint(c.Param("id"), 10, 32)
		c_, err := repo.GetByID(uint(id))
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(404, "支付渠道不存在"))
			return
		}
		c.JSON(http.StatusOK, response.OK(c_))
	}
}

func channelCreate(repo *repository.PaymentChannelRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		var req struct {
			ChannelCode string `json:"channelCode" binding:"required"`
			InterfaceID uint   `json:"interfaceId" binding:"required"`
			Params      string `json:"params"`
			RiskConfig  string `json:"riskConfig"`
			Status      *int8  `json:"status"`
		}
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusOK, response.Fail(400, err.Error()))
			return
		}
		if _, err := repo.GetByChannelCode(req.ChannelCode); err == nil {
			c.JSON(http.StatusOK, response.Fail(400, "渠道编码已存在"))
			return
		}
		status := int8(1)
		if req.Status != nil {
			status = *req.Status
		}
		c_ := &entity.PaymentChannel{ChannelCode: req.ChannelCode, InterfaceID: req.InterfaceID, Params: req.Params, RiskConfig: req.RiskConfig, Status: status}
		if err := repo.Create(c_); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK(c_.ID))
	}
}

func channelUpdate(repo *repository.PaymentChannelRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		id, _ := strconv.ParseUint(c.Param("id"), 10, 32)
		c_, err := repo.GetByID(uint(id))
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(404, "支付渠道不存在"))
			return
		}
		var req struct {
			ChannelCode string `json:"channelCode"`
			InterfaceID *uint  `json:"interfaceId"`
			Params      string `json:"params"`
			RiskConfig  string `json:"riskConfig"`
			Status      *int8  `json:"status"`
		}
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusOK, response.Fail(400, err.Error()))
			return
		}
		if req.ChannelCode != "" {
			c_.ChannelCode = req.ChannelCode
		}
		if req.InterfaceID != nil {
			c_.InterfaceID = *req.InterfaceID
		}
		if req.Params != "" {
			c_.Params = req.Params
		}
		if req.RiskConfig != "" {
			c_.RiskConfig = req.RiskConfig
		}
		if req.Status != nil {
			c_.Status = *req.Status
		}
		if err := repo.Update(c_); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK[any](nil))
	}
}

// 商户-支付产品关联
func merchantProducts(mpRepo *repository.MerchantProductRepo, productRepo *repository.PaymentProductRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		id, _ := strconv.ParseUint(c.Param("id"), 10, 32)
		merchantID := uint(id)
		ids, err := mpRepo.ListProductIDsByMerchantID(merchantID)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		list := make([]*entity.PaymentProduct, 0, len(ids))
		for _, pid := range ids {
			p, err := productRepo.GetByID(pid)
			if err != nil {
				continue
			}
			list = append(list, p)
		}
		c.JSON(http.StatusOK, response.OK(list))
	}
}

func merchantProductAdd(mpRepo *repository.MerchantProductRepo, merchantRepo *repository.MerchantRepo, productRepo *repository.PaymentProductRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		id, _ := strconv.ParseUint(c.Param("id"), 10, 32)
		merchantID := uint(id)
		var req struct {
			ProductID uint `json:"productId" binding:"required"`
		}
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusOK, response.Fail(400, err.Error()))
			return
		}
		if _, err := merchantRepo.GetByID(merchantID); err != nil {
			c.JSON(http.StatusOK, response.Fail(404, "商户不存在"))
			return
		}
		if _, err := productRepo.GetByID(req.ProductID); err != nil {
			c.JSON(http.StatusOK, response.Fail(404, "支付产品不存在"))
			return
		}
		mp := &entity.MerchantProduct{MerchantID: merchantID, ProductID: req.ProductID}
		if err := mpRepo.Create(mp); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK[any](nil))
	}
}

func merchantProductRemove(mpRepo *repository.MerchantProductRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		id, _ := strconv.ParseUint(c.Param("id"), 10, 32)
		pid, _ := strconv.ParseUint(c.Param("productId"), 10, 32)
		if err := mpRepo.DeleteByMerchantAndProduct(uint(id), uint(pid)); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK[any](nil))
	}
}

// 产品-渠道关联
func productChannels(pcRepo *repository.ProductChannelRepo, channelRepo *repository.PaymentChannelRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		id, _ := strconv.ParseUint(c.Param("id"), 10, 32)
		productID := uint(id)
		list, err := pcRepo.ListByProductID(productID)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		type item struct {
			entity.ProductChannel
			ChannelCode string `json:"channelCode"`
			ChannelName string `json:"channelName"`
		}
		result := make([]item, 0, len(list))
		for _, pc := range list {
			ch, _ := channelRepo.GetByID(pc.ChannelID)
			code, name := "", ""
			if ch != nil {
				code = ch.ChannelCode
				name = ch.ChannelCode
			}
			result = append(result, item{ProductChannel: pc, ChannelCode: code, ChannelName: name})
		}
		c.JSON(http.StatusOK, response.OK(result))
	}
}

func productChannelAdd(pcRepo *repository.ProductChannelRepo, productRepo *repository.PaymentProductRepo, channelRepo *repository.PaymentChannelRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		id, _ := strconv.ParseUint(c.Param("id"), 10, 32)
		productID := uint(id)
		var req struct {
			ChannelID  uint `json:"channelId" binding:"required"`
			SortOrder  int  `json:"sortOrder"`
		}
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusOK, response.Fail(400, err.Error()))
			return
		}
		if _, err := productRepo.GetByID(productID); err != nil {
			c.JSON(http.StatusOK, response.Fail(404, "支付产品不存在"))
			return
		}
		if _, err := channelRepo.GetByID(req.ChannelID); err != nil {
			c.JSON(http.StatusOK, response.Fail(404, "支付渠道不存在"))
			return
		}
		pc := &entity.ProductChannel{ProductID: productID, ChannelID: req.ChannelID, SortOrder: req.SortOrder}
		if err := pcRepo.Create(pc); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK[any](nil))
	}
}

func productChannelSort(pcRepo *repository.ProductChannelRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		id, _ := strconv.ParseUint(c.Param("id"), 10, 32)
		cid, _ := strconv.ParseUint(c.Param("channelId"), 10, 32)
		var req struct {
			SortOrder int `json:"sortOrder"`
		}
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusOK, response.Fail(400, err.Error()))
			return
		}
		if err := pcRepo.UpdateSortOrder(uint(id), uint(cid), req.SortOrder); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK[any](nil))
	}
}

func productChannelRemove(pcRepo *repository.ProductChannelRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		id, _ := strconv.ParseUint(c.Param("id"), 10, 32)
		cid, _ := strconv.ParseUint(c.Param("channelId"), 10, 32)
		if err := pcRepo.DeleteByProductAndChannel(uint(id), uint(cid)); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK[any](nil))
	}
}

// reconcileExport 对账单导出：按日期、商户、时间类型（创建时间/更新时间）查询支付/退款/流水明细与汇总
// GET /manager/reconcile/export?date=2025-02-23&merchantId=1&timeType=created|updated（默认 created）
func reconcileExport(payOrderRepo *repository.PayOrderRepo, refundOrderRepo *repository.RefundOrderRepo, accountRepo *repository.AccountRepo, accountFlowRepo *repository.AccountFlowRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		dateStr := c.Query("date")
		merchantIdStr := c.Query("merchantId")
		timeType := c.DefaultQuery("timeType", "created")
		if timeType != "created" && timeType != "updated" {
			timeType = "created"
		}
		if dateStr == "" || merchantIdStr == "" {
			c.JSON(http.StatusOK, response.Fail(400, "date 与 merchantId 必填，格式 date=yyyy-MM-dd"))
			return
		}
		date, err := time.Parse("2006-01-02", dateStr)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(400, "date 格式为 yyyy-MM-dd"))
			return
		}
		merchantID, _ := strconv.ParseUint(merchantIdStr, 10, 32)
		if merchantID == 0 {
			c.JSON(http.StatusOK, response.Fail(400, "merchantId 无效"))
			return
		}
		start := time.Date(date.Year(), date.Month(), date.Day(), 0, 0, 0, 0, date.Location())
		end := start.Add(24 * time.Hour)

		var payList []entity.PayOrder
		var refundList []entity.RefundOrder
		if timeType == "updated" {
			payList, err = payOrderRepo.ListByMerchantIDAndDateByUpdated(uint(merchantID), start, end)
		} else {
			payList, err = payOrderRepo.ListByMerchantIDAndDate(uint(merchantID), start, end)
		}
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		if timeType == "updated" {
			refundList, err = refundOrderRepo.ListByMerchantIDAndDateByUpdated(uint(merchantID), start, end)
		} else {
			refundList, err = refundOrderRepo.ListByMerchantIDAndDate(uint(merchantID), start, end)
		}
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		acc, err := accountRepo.GetByMerchantID(uint(merchantID))
		var flowList []entity.AccountFlow
		if err == nil && acc != nil {
			// 流水表无 updated_at，统一按创建时间查
			flowList, _ = accountFlowRepo.ListByAccountIDAndDate(acc.ID, start, end)
		}

		var payAmount, refundAmount int64
		for _, o := range payList {
			if o.Status == 1 {
				payAmount += o.Amount
			}
		}
		for _, o := range refundList {
			if o.Status == 1 {
				refundAmount += o.Amount
			}
		}

		c.JSON(http.StatusOK, response.OK(gin.H{
			"date":         dateStr,
			"merchantId":   merchantID,
			"timeType":     timeType,
			"payOrders":    payList,
			"refundOrders": refundList,
			"accountFlows": flowList,
			"summary": gin.H{
				"payCount":     len(payList),
				"payAmount":    payAmount,
				"refundCount":  len(refundList),
				"refundAmount": refundAmount,
			},
		}))
	}
}

// reconcileReports 查询某日对账汇总（job 生成），GET /manager/reconcile/reports?date=yyyy-MM-dd&timeType=created|updated（可选，不传则返回两种）
func reconcileReports(repo *repository.ReconcileReportRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		dateStr := c.Query("date")
		timeType := c.Query("timeType")
		if dateStr == "" {
			c.JSON(http.StatusOK, response.Fail(400, "date 必填，格式 yyyy-MM-dd"))
			return
		}
		date, err := time.Parse("2006-01-02", dateStr)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(400, "date 格式为 yyyy-MM-dd"))
			return
		}
		list, err := repo.ListByDate(date)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		if timeType == "created" || timeType == "updated" {
			filtered := make([]entity.ReconcileReport, 0, len(list))
			for _, r := range list {
				if r.TimeType == timeType {
					filtered = append(filtered, r)
				}
			}
			list = filtered
		}
		c.JSON(http.StatusOK, response.OK(list))
	}
}

// 分润规则
func profitSharePage(repo *repository.ProfitShareRuleRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		page, _ := strconv.ParseInt(c.DefaultQuery("current", "1"), 10, 64)
		size, _ := strconv.ParseInt(c.DefaultQuery("size", "20"), 10, 64)
		var agentID, merchantID *uint
		if s := c.Query("agentId"); s != "" {
			v, _ := strconv.ParseUint(s, 10, 32)
			u := uint(v)
			agentID = &u
		}
		if s := c.Query("merchantId"); s != "" {
			v, _ := strconv.ParseUint(s, 10, 32)
			u := uint(v)
			merchantID = &u
		}
		list, total, err := repo.List(page, size, agentID, merchantID)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK(response.PageResult[entity.ProfitShareRule]{
			Current: page, Size: size, Total: total, Records: list,
		}))
	}
}

func profitShareGet(repo *repository.ProfitShareRuleRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		id, _ := strconv.ParseUint(c.Param("id"), 10, 32)
		row, err := repo.GetByID(uint(id))
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(404, "分润规则不存在"))
			return
		}
		c.JSON(http.StatusOK, response.OK(row))
	}
}

func profitShareCreate(repo *repository.ProfitShareRuleRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		var req struct {
			AgentID    *uint  `json:"agentId"`
			MerchantID *uint  `json:"merchantId"`
			ProductID  *uint  `json:"productId"`
			Rate       int    `json:"rate" binding:"required"`
			Remark     string `json:"remark"`
		}
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusOK, response.Fail(400, err.Error()))
			return
		}
		if req.Rate < 0 || req.Rate > 10000 {
			c.JSON(http.StatusOK, response.Fail(400, "rate 为万分比 0-10000"))
			return
		}
		row := &entity.ProfitShareRule{
			AgentID: req.AgentID, MerchantID: req.MerchantID, ProductID: req.ProductID,
			Rate: req.Rate, Remark: req.Remark,
		}
		if err := repo.Create(row); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK(row.ID))
	}
}

func profitShareUpdate(repo *repository.ProfitShareRuleRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		id, _ := strconv.ParseUint(c.Param("id"), 10, 32)
		row, err := repo.GetByID(uint(id))
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(404, "分润规则不存在"))
			return
		}
		var req struct {
			AgentID    *uint  `json:"agentId"`
			MerchantID *uint  `json:"merchantId"`
			ProductID  *uint  `json:"productId"`
			Rate       *int   `json:"rate"`
			Remark     string `json:"remark"`
		}
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusOK, response.Fail(400, err.Error()))
			return
		}
		if req.AgentID != nil {
			row.AgentID = req.AgentID
		}
		if req.MerchantID != nil {
			row.MerchantID = req.MerchantID
		}
		if req.ProductID != nil {
			row.ProductID = req.ProductID
		}
		if req.Rate != nil && *req.Rate >= 0 && *req.Rate <= 10000 {
			row.Rate = *req.Rate
		}
		if req.Remark != "" {
			row.Remark = req.Remark
		}
		if err := repo.Update(row); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK[any](nil))
	}
}

func profitShareDelete(repo *repository.ProfitShareRuleRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		id, _ := strconv.ParseUint(c.Param("id"), 10, 32)
		if err := repo.Delete(uint(id)); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK[any](nil))
	}
}

// 提现管理
func withdrawPage(repo *repository.WithdrawOrderRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		page, _ := strconv.ParseInt(c.DefaultQuery("current", "1"), 10, 64)
		size, _ := strconv.ParseInt(c.DefaultQuery("size", "20"), 10, 64)
		var merchantID *uint
		var status *int8
		if s := c.Query("merchantId"); s != "" {
			v, _ := strconv.ParseUint(s, 10, 32)
			u := uint(v)
			merchantID = &u
		}
		if s := c.Query("status"); s != "" {
			v, _ := strconv.ParseInt(s, 10, 8)
			t := int8(v)
			status = &t
		}
		list, total, err := repo.List(page, size, merchantID, status)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK(response.PageResult[entity.WithdrawOrder]{
			Current: page, Size: size, Total: total, Records: list,
		}))
	}
}

func withdrawGet(repo *repository.WithdrawOrderRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		id, _ := strconv.ParseUint(c.Param("id"), 10, 32)
		o, err := repo.GetByID(uint(id))
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(404, "提现单不存在"))
			return
		}
		c.JSON(http.StatusOK, response.OK(o))
	}
}

func withdrawAudit(db *gorm.DB, withdrawRepo *repository.WithdrawOrderRepo, accountRepo *repository.AccountRepo, auditLogRepo *repository.AuditLogRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		id, _ := strconv.ParseUint(c.Param("id"), 10, 32)
		o, err := withdrawRepo.GetByID(uint(id))
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(404, "提现单不存在"))
			return
		}
		if o.Status != entity.WithdrawStatusPending {
			c.JSON(http.StatusOK, response.Fail(400, "仅待审核状态可操作"))
			return
		}
		var req struct {
			Status      int8   `json:"status" binding:"required"` // 1通过 2拒绝
			AuditRemark string `json:"auditRemark"`
		}
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusOK, response.Fail(400, err.Error()))
			return
		}
		if req.Status != entity.WithdrawStatusPass && req.Status != entity.WithdrawStatusReject {
			c.JSON(http.StatusOK, response.Fail(400, "status 1=通过 2=拒绝"))
			return
		}
		o.Status = req.Status
		o.AuditRemark = req.AuditRemark
		if req.Status == entity.WithdrawStatusPass {
			acc, err := accountRepo.GetByMerchantID(o.MerchantID)
			if err != nil || acc == nil {
				c.JSON(http.StatusOK, response.Fail(400, "商户账户不存在"))
				return
			}
			if acc.Balance < o.Amount {
				c.JSON(http.StatusOK, response.Fail(400, "商户余额不足"))
				return
			}
			if err := service.AddBalance(db, o.MerchantID, -o.Amount, "withdraw", "W"+strconv.Itoa(int(o.ID))); err != nil {
				c.JSON(http.StatusOK, response.Fail(500, err.Error()))
				return
			}
		}
		if err := withdrawRepo.Update(o); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		audit.LogAdmin(c, auditLogRepo, "withdraw_audit", "withdraw_order", c.Param("id"), gin.H{"status": req.Status, "auditRemark": req.AuditRemark})
		c.JSON(http.StatusOK, response.OK[any](nil))
	}
}
