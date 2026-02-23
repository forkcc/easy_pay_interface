package entity

import "time"

type BotChatBind struct {
	ID        uint      `gorm:"primaryKey"`
	ChatID    int64     `gorm:"uniqueIndex;not null"`
	BindType  string    `gorm:"size:16;not null"` // merchant | channel
	BindID    string    `gorm:"size:64;not null"` // merchant_id or interface_code
	CreatedAt time.Time
	UpdatedAt time.Time
}

func (BotChatBind) TableName() string { return "bot_chat_bind" }

const (
	BotBindTypeMerchant = "merchant"
	BotBindTypeChannel  = "channel"
)
