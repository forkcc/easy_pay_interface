package entity

import "time"

type RefundOrder struct {
	ID            uint      `gorm:"primaryKey"`
	RefundOrderNo string    `gorm:"uniqueIndex;size:64;not null"`
	OutRefundNo   string    `gorm:"size:64"`
	PayOrderID    uint      `gorm:"not null"`
	MerchantID    uint      `gorm:"not null"`
	Amount        int64     `gorm:"not null"`
	Status        int8      `gorm:"not null;default:0"`
	UpstreamSn    string    `gorm:"size:128"`
	CreatedAt     time.Time
	UpdatedAt     time.Time
}

func (RefundOrder) TableName() string { return "refund_order" }
