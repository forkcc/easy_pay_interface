package entity

import "time"

type Account struct {
	ID         uint      `gorm:"primaryKey"`
	MerchantID uint      `gorm:"uniqueIndex;not null"`
	Balance    int64     `gorm:"not null;default:0"`
	Frozen     int64     `gorm:"not null;default:0"`
	Version    int       `gorm:"not null;default:0"`
	CreatedAt  time.Time
	UpdatedAt  time.Time
}

func (Account) TableName() string { return "account" }
