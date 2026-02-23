package entity

import "time"

type PaymentInterface struct {
	ID            uint      `gorm:"primaryKey"`
	InterfaceCode string    `gorm:"uniqueIndex;size:32;not null"`
	Name          string    `gorm:"size:64;not null"`
	RequestTpl    string    `gorm:"type:text"`
	Status        int8      `gorm:"not null;default:1"`
	CreatedAt     time.Time
	UpdatedAt     time.Time
}

func (PaymentInterface) TableName() string { return "payment_interface" }
