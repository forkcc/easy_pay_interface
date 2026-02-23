package audit

import (
	"github.com/easypay/easy_pay_interface/internal/entity"
	"github.com/easypay/easy_pay_interface/internal/middleware"
	"github.com/easypay/easy_pay_interface/internal/repository"
	"github.com/gin-gonic/gin"
)

func LogAdmin(c *gin.Context, repo *repository.AuditLogRepo, action, targetType, targetID string, detail interface{}) {
	if repo == nil {
		return
	}
	userID := middleware.GetAdminUserID(c)
	username := middleware.GetAdminUsername(c)
	ent := &entity.AuditLog{
		UserType:   "admin",
		UserID:     userID,
		Username:   username,
		Action:     action,
		TargetType: targetType,
		TargetID:   targetID,
		IP:         c.ClientIP(),
	}
	ent.SetDetailJSON(detail)
	_ = repo.Create(ent)
}

func LogAgent(c *gin.Context, repo *repository.AuditLogRepo, action, targetType, targetID string, detail interface{}) {
	if repo == nil {
		return
	}
	userID := middleware.GetAgentUserID(c)
	username := middleware.GetAgentUsername(c)
	ent := &entity.AuditLog{
		UserType:   "agent",
		UserID:     userID,
		Username:   username,
		Action:     action,
		TargetType: targetType,
		TargetID:   targetID,
		IP:         c.ClientIP(),
	}
	ent.SetDetailJSON(detail)
	_ = repo.Create(ent)
}
