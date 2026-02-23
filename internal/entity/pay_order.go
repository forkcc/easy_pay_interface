package entity

import "time"

type PayOrder struct {
	ID           uint      `gorm:"primaryKey"`
	PayOrderNo   string    `gorm:"uniqueIndex;size:64;not null"`
	OutTradeNo   string    `gorm:"size:64"`
	MerchantID   uint      `gorm:"not null"`
	ChannelID    uint      `gorm:"not null"`
	Amount       int64     `gorm:"not null"`
	Status       int8      `gorm:"not null;default:0"`
	UpstreamSn   string
	NotifyStatus int8      `gorm:"not null;default:0"`
	CreatedAt    time.Time
	UpdatedAt    time.Time
}

func (PayOrder) TableName() string { return "pay_order" }
