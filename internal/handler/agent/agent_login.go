package agent

import (
	"net/http"

	"github.com/easypay/easy_pay_interface/internal/response"
	"github.com/easypay/easy_pay_interface/internal/repository"
	"github.com/easypay/easy_pay_interface/internal/service"
	"github.com/gin-gonic/gin"
)

func agentLogin(agentRepo *repository.AgentRepo, agentUserRepo *repository.AgentUserRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		var req struct {
			AgentNo  string `json:"agentNo" binding:"required"`
			Username string `json:"username" binding:"required"`
			Password string `json:"password" binding:"required"`
		}
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusOK, response.Fail(400, "参数错误"))
			return
		}
		agent, err := agentRepo.GetByAgentNo(req.AgentNo)
		if err != nil || agent == nil {
			c.JSON(http.StatusOK, response.Fail(401, "代理号或用户名或密码错误"))
			return
		}
		u, err := agentUserRepo.GetByAgentIDAndUsername(agent.ID, req.Username)
		if err != nil || u == nil {
			c.JSON(http.StatusOK, response.Fail(401, "代理号或用户名或密码错误"))
			return
		}
		if !service.CheckPassword(u.PasswordHash, req.Password) {
			c.JSON(http.StatusOK, response.Fail(401, "代理号或用户名或密码错误"))
			return
		}
		token, exp, err := service.IssueAgentToken(u.ID, u.Username, u.AgentID)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(500, "生成令牌失败"))
			return
		}
		c.JSON(http.StatusOK, response.OK(gin.H{
			"token":    token,
			"expireAt": exp.Format("2006-01-02T15:04:05Z07:00"),
			"user": gin.H{
				"id":       u.ID,
				"username": u.Username,
				"name":     u.Name,
				"agentId":  u.AgentID,
			},
		}))
	}
}
