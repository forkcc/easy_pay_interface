package middleware

import (
	"strconv"

	"github.com/easypay/easy_pay_interface/internal/repository"
	"github.com/gin-gonic/gin"
)

const (
	HeaderMerchantID = "X-Merchant-Id"
	HeaderAgentID    = "X-Agent-Id"
	HeaderApiKey     = "X-Api-Key"
	CtxMerchantID    = "merchant_id"
	CtxAgentID       = "agent_id"
	defaultID        = uint(1)
)

// MerchantID 从 Header 取商户 ID：优先 X-Api-Key 查商户，否则 X-Merchant-Id，否则 defaultID（仅当 requireAuth=false 时使用默认值）
func MerchantID(merchantRepo *repository.MerchantRepo, requireAuth bool) gin.HandlerFunc {
	return func(c *gin.Context) {
		if merchantRepo != nil {
			if apiKey := c.GetHeader(HeaderApiKey); apiKey != "" {
				m, err := merchantRepo.GetByApiKey(apiKey)
				if err == nil && m != nil {
					c.Set(CtxMerchantID, m.ID)
					c.Next()
					return
				}
			}
		}
		if v := c.GetHeader(HeaderMerchantID); v != "" {
			if id, err := strconv.ParseUint(v, 10, 32); err == nil {
				c.Set(CtxMerchantID, uint(id))
				c.Next()
				return
			}
		}
		if requireAuth {
			c.JSON(200, gin.H{"code": 401, "msg": "缺少 X-Api-Key 或 X-Merchant-Id"})
			c.Abort()
			return
		}
		c.Set(CtxMerchantID, defaultID)
		c.Next()
	}
}

// AgentID 从 Header 取代理 ID：优先 X-Api-Key 查代理，否则 X-Agent-Id，否则默认 1
func AgentID(agentRepo *repository.AgentRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		if agentRepo != nil {
			if apiKey := c.GetHeader(HeaderApiKey); apiKey != "" {
				a, err := agentRepo.GetByApiKey(apiKey)
				if err == nil && a != nil {
					c.Set(CtxAgentID, a.ID)
					c.Next()
					return
				}
			}
		}
		if v := c.GetHeader(HeaderAgentID); v != "" {
			if id, err := strconv.ParseUint(v, 10, 32); err == nil {
				c.Set(CtxAgentID, uint(id))
				c.Next()
				return
			}
		}
		c.Set(CtxAgentID, defaultID)
		c.Next()
	}
}

// GetMerchantID 从 context 取商户 ID（需先挂 MerchantID 中间件）
func GetMerchantID(c *gin.Context) uint {
	v, _ := c.Get(CtxMerchantID)
	if id, ok := v.(uint); ok {
		return id
	}
	return defaultID
}

// GetAgentID 从 context 取代理 ID（需先挂 AgentID 中间件）
func GetAgentID(c *gin.Context) uint {
	v, _ := c.Get(CtxAgentID)
	if id, ok := v.(uint); ok {
		return id
	}
	return defaultID
}
