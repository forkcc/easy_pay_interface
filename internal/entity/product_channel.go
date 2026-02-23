package entity

import "time"

type ProductChannel struct {
	ID        uint      `gorm:"primaryKey"`
	ProductID uint      `gorm:"uniqueIndex:uk_pc;not null"`
	ChannelID uint      `gorm:"uniqueIndex:uk_pc;not null"`
	SortOrder int       `gorm:"not null;default:0"`
	CreatedAt time.Time
}

func (ProductChannel) TableName() string { return "product_channel" }
