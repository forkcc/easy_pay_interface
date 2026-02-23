package entity

import "time"

type ProfitShareRule struct {
	ID         uint      `gorm:"primaryKey"`
	AgentID    *uint     `gorm:""`
	MerchantID *uint     `gorm:""`
	ProductID  *uint     `gorm:""`
	Rate       int       `gorm:"not null;default:0"` // 万分比，1000=10%
	Remark     string    `gorm:"size:128"`
	CreatedAt  time.Time
	UpdatedAt  time.Time
}

func (ProfitShareRule) TableName() string { return "profit_share_rule" }
