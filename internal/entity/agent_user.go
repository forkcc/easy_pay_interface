package entity

import "time"

type AgentUser struct {
	ID           uint      `gorm:"primaryKey"`
	AgentID      uint      `gorm:"not null"`
	Username     string    `gorm:"size:64;not null"`
	PasswordHash string    `gorm:"size:128;not null"`
	Name         string    `gorm:"size:64;not null"`
	Status       int8      `gorm:"not null;default:1"`
	CreatedAt    time.Time
	UpdatedAt    time.Time
}

func (AgentUser) TableName() string { return "agent_user" }
