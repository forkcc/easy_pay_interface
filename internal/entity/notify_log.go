package entity

import "time"

type NotifyLog struct {
	ID          uint      `gorm:"primaryKey"`
	BizType     string    `gorm:"size:16;not null"`
	BizID       uint      `gorm:"not null"`
	NotifyURL   string    `gorm:"size:512;not null"`
	ReqBody     string    `gorm:"type:text"`
	RespStatus  *int
	RespBody    string    `gorm:"type:text"`
	RetryCount  int       `gorm:"not null;default:0"`
	CreatedAt   time.Time
	UpdatedAt   time.Time
}

func (NotifyLog) TableName() string { return "notify_log" }
