package agent

import (
	"net/http"
	"strconv"

	"github.com/easypay/easy_pay_interface/internal/entity"
	"github.com/easypay/easy_pay_interface/internal/middleware"
	"github.com/easypay/easy_pay_interface/internal/response"
	"github.com/easypay/easy_pay_interface/internal/repository"
	"github.com/gin-gonic/gin"
)

func Routes(g *gin.RouterGroup, agentRepo *repository.AgentRepo, agentUserRepo *repository.AgentUserRepo, agentMerchantRepo *repository.AgentMerchantRepo, merchantRepo *repository.MerchantRepo, auditLogRepo *repository.AuditLogRepo) {
	g.GET("/health", func(c *gin.Context) { c.JSON(http.StatusOK, response.OK("ok")) })
	g.POST("/login", agentLogin(agentRepo, agentUserRepo))
	g.Use(middleware.AgentAuth())
	g.GET("/merchant/page", merchantPage(agentMerchantRepo, merchantRepo))
}

func merchantPage(amRepo *repository.AgentMerchantRepo, merchantRepo *repository.MerchantRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		agentID := middleware.GetAgentID(c)
		page, _ := strconv.ParseInt(c.DefaultQuery("current", "1"), 10, 64)
		size, _ := strconv.ParseInt(c.DefaultQuery("size", "20"), 10, 64)
		merchantIDs, total, err := amRepo.PageMerchantIDsByAgentID(agentID, page, size)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		if len(merchantIDs) == 0 {
			c.JSON(http.StatusOK, response.OK(response.PageResult[entity.Merchant]{
				Current: page, Size: size, Total: total, Records: []entity.Merchant{},
			}))
			return
		}
		var list []entity.Merchant
		for _, id := range merchantIDs {
			m, err := merchantRepo.GetByID(id)
			if err != nil {
				continue
			}
			list = append(list, *m)
		}
		c.JSON(http.StatusOK, response.OK(response.PageResult[entity.Merchant]{
			Current: page, Size: size, Total: total, Records: list,
		}))
	}
}
