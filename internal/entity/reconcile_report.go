package entity

import "time"

const (
	ReconcileTimeTypeCreated = "created"
	ReconcileTimeTypeUpdated = "updated"
)

type ReconcileReport struct {
	ID            uint      `gorm:"primaryKey"`
	ReconcileDate time.Time `gorm:"type:date;not null;uniqueIndex:uk_date_merchant_time"`
	MerchantID    uint      `gorm:"not null;uniqueIndex:uk_date_merchant_time"`
	TimeType      string    `gorm:"type:varchar(16);not null;default:created;uniqueIndex:uk_date_merchant_time"` // created | updated
	PayCount      int       `gorm:"not null;default:0"`
	PayAmount     int64     `gorm:"not null;default:0"`
	RefundCount   int       `gorm:"not null;default:0"`
	RefundAmount  int64     `gorm:"not null;default:0"`
	CreatedAt     time.Time
	UpdatedAt     time.Time
}

func (ReconcileReport) TableName() string { return "reconcile_report" }
