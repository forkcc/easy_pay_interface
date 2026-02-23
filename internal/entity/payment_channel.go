package entity

import "time"

type PaymentChannel struct {
	ID          uint      `gorm:"primaryKey"`
	ChannelCode string    `gorm:"uniqueIndex;size:32;not null"`
	InterfaceID uint      `gorm:"not null"`
	Params      string    `gorm:"type:text"`
	RiskConfig  string    `gorm:"type:text"`
	Status      int8      `gorm:"not null;default:1"`
	CreatedAt   time.Time
	UpdatedAt   time.Time
}

func (PaymentChannel) TableName() string { return "payment_channel" }
