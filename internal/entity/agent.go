package entity

import "time"

type Agent struct {
	ID        uint      `gorm:"primaryKey"`
	AgentNo   string    `gorm:"uniqueIndex;size:32;not null"`
	Name      string    `gorm:"size:64;not null"`
	ParentID  *uint
	Status    int8      `gorm:"not null;default:1"`
	ApiKey    string    `gorm:"size:64;index"`
	CreatedAt time.Time
	UpdatedAt time.Time
}

func (Agent) TableName() string { return "agent" }
