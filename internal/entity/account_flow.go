package entity

import "time"

type AccountFlow struct {
	ID          uint      `gorm:"primaryKey"`
	AccountID   uint      `gorm:"not null"`
	BizType     string    `gorm:"size:16;not null"`
	BizSn       string    `gorm:"size:64;not null"`
	Amount      int64     `gorm:"not null"`
	BalanceAfter int64    `gorm:"not null"`
	CreatedAt   time.Time
}

func (AccountFlow) TableName() string { return "account_flow" }
