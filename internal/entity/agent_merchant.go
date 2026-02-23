package entity

import "time"

type AgentMerchant struct {
	ID         uint      `gorm:"primaryKey"`
	AgentID    uint      `gorm:"uniqueIndex:uk_am;not null"`
	MerchantID uint      `gorm:"uniqueIndex:uk_am;not null"`
	CreatedAt  time.Time
}

func (AgentMerchant) TableName() string { return "agent_merchant" }
