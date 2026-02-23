package entity

import "time"

type MerchantProduct struct {
	ID         uint      `gorm:"primaryKey"`
	MerchantID uint      `gorm:"uniqueIndex:uk_mp;not null"`
	ProductID  uint      `gorm:"uniqueIndex:uk_mp;not null"`
	CreatedAt  time.Time
}

func (MerchantProduct) TableName() string { return "merchant_product" }
