package entity

import "time"

type AdminUser struct {
	ID           uint      `gorm:"primaryKey"`
	Username     string    `gorm:"size:64;uniqueIndex;not null"`
	PasswordHash string    `gorm:"size:128;not null"`
	Name         string    `gorm:"size:64;not null"`
	Status       int8      `gorm:"not null;default:1"`
	CreatedAt    time.Time
	UpdatedAt    time.Time
}

func (AdminUser) TableName() string { return "admin_user" }
