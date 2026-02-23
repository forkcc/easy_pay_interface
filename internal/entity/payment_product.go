package entity

import "time"

type PaymentProduct struct {
	ID          uint      `gorm:"primaryKey"`
	ProductCode string    `gorm:"uniqueIndex;size:32;not null"`
	Name        string    `gorm:"size:64;not null"`
	Status      int8      `gorm:"not null;default:1"`
	CreatedAt   time.Time
	UpdatedAt   time.Time
}

func (PaymentProduct) TableName() string { return "payment_product" }
