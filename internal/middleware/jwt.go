package middleware

import (
	"net/http"
	"strings"

	"github.com/easypay/easy_pay_interface/internal/response"
	"github.com/easypay/easy_pay_interface/internal/service"
	"github.com/gin-gonic/gin"
)

const (
	CtxAdminUserID   = "admin_user_id"
	CtxAdminUsername = "admin_username"
	CtxAgentUserID   = "agent_user_id"
	CtxAgentUsername = "agent_username"
)

func extractBearer(c *gin.Context) string {
	h := c.GetHeader("Authorization")
	if strings.HasPrefix(h, "Bearer ") {
		return strings.TrimPrefix(h, "Bearer ")
	}
	return ""
}

// AdminAuth 管理端 JWT 鉴权，未登录返回 401
func AdminAuth() gin.HandlerFunc {
	return func(c *gin.Context) {
		token := extractBearer(c)
		if token == "" {
			c.JSON(http.StatusOK, response.Fail(401, "未登录或登录已过期"))
			c.Abort()
			return
		}
		claims, err := service.ParseAdminToken(token)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(401, "未登录或登录已过期"))
			c.Abort()
			return
		}
		c.Set(CtxAdminUserID, claims.UserID)
		c.Set(CtxAdminUsername, claims.Username)
		c.Next()
	}
}

// AgentAuth 代理端 JWT 鉴权，并将 agent_id 写入 context（供后续用 GetAgentID）
func AgentAuth() gin.HandlerFunc {
	return func(c *gin.Context) {
		token := extractBearer(c)
		if token == "" {
			c.JSON(http.StatusOK, response.Fail(401, "未登录或登录已过期"))
			c.Abort()
			return
		}
		claims, err := service.ParseAgentToken(token)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(401, "未登录或登录已过期"))
			c.Abort()
			return
		}
		c.Set(CtxAgentUserID, claims.UserID)
		c.Set(CtxAgentUsername, claims.Username)
		c.Set(CtxAgentID, claims.AgentID)
		c.Next()
	}
}

func GetAdminUserID(c *gin.Context) uint {
	v, _ := c.Get(CtxAdminUserID)
	if id, ok := v.(uint); ok {
		return id
	}
	return 0
}

func GetAdminUsername(c *gin.Context) string {
	v, _ := c.Get(CtxAdminUsername)
	if s, ok := v.(string); ok {
		return s
	}
	return ""
}

func GetAgentUserID(c *gin.Context) uint {
	v, _ := c.Get(CtxAgentUserID)
	if id, ok := v.(uint); ok {
		return id
	}
	return 0
}

func GetAgentUsername(c *gin.Context) string {
	v, _ := c.Get(CtxAgentUsername)
	if s, ok := v.(string); ok {
		return s
	}
	return ""
}
