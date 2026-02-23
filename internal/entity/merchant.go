package entity

import "time"

type Merchant struct {
	ID         uint      `gorm:"primaryKey"`
	MerchantNo string    `gorm:"uniqueIndex;size:32;not null"`
	Name       string    `gorm:"size:64;not null"`
	Status     int8      `gorm:"not null;default:1"`
	NotifyURL  string    `gorm:"size:512"`
	ApiKey     string    `gorm:"size:64;index"`
	CreatedAt  time.Time
	UpdatedAt  time.Time
}

func (Merchant) TableName() string { return "merchant" }
