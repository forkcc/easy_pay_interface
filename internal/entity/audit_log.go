package entity

import (
	"encoding/json"
	"time"
)

type AuditLog struct {
	ID         uint      `gorm:"primaryKey"`
	UserType   string    `gorm:"size:16;not null"`   // admin / agent
	UserID     uint      `gorm:"not null"`
	Username   string    `gorm:"size:64;not null"`
	Action     string    `gorm:"size:64;not null"`
	TargetType string    `gorm:"size:32;not null"`
	TargetID   string    `gorm:"size:64;not null"`
	Detail     string    `gorm:"type:jsonb"` // JSON
	IP         string    `gorm:"size:64;not null"`
	CreatedAt  time.Time `gorm:"not null"`
}

func (AuditLog) TableName() string { return "audit_log" }

func (a *AuditLog) SetDetailJSON(v interface{}) {
	if v == nil {
		a.Detail = ""
		return
	}
	b, _ := json.Marshal(v)
	a.Detail = string(b)
}
