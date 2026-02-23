package entity

import "time"

type WithdrawOrder struct {
	ID          uint      `gorm:"primaryKey"`
	MerchantID  uint      `gorm:"not null"`
	Amount      int64     `gorm:"not null"`
	Status      int8      `gorm:"not null;default:0"` // 0待审核 1已通过 2已拒绝
	AuditRemark string    `gorm:"size:256"`
	CreatedAt   time.Time
	UpdatedAt   time.Time
}

func (WithdrawOrder) TableName() string { return "withdraw_order" }

const (
	WithdrawStatusPending = 0
	WithdrawStatusPass    = 1
	WithdrawStatusReject  = 2
)
