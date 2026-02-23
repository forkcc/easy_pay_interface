package repository

import (
	"time"

	"github.com/easypay/easy_pay_interface/internal/entity"
	"gorm.io/gorm"
)

type AuditLogRepo struct{ db *gorm.DB }

func NewAuditLogRepo(db *gorm.DB) *AuditLogRepo { return &AuditLogRepo{db: db} }

func (r *AuditLogRepo) Create(log *entity.AuditLog) error { return r.db.Create(log).Error }

func (r *AuditLogRepo) List(page, size int64, userType, action string, startTime, endTime *time.Time) ([]entity.AuditLog, int64, error) {
	q := r.db.Model(&entity.AuditLog{})
	if userType != "" {
		q = q.Where("user_type = ?", userType)
	}
	if action != "" {
		q = q.Where("action = ?", action)
	}
	if startTime != nil {
		q = q.Where("created_at >= ?", *startTime)
	}
	if endTime != nil {
		q = q.Where("created_at <= ?", *endTime)
	}
	var total int64
	if err := q.Count(&total).Error; err != nil {
		return nil, 0, err
	}
	var list []entity.AuditLog
	offset := (page - 1) * size
	if err := q.Order("id DESC").Offset(int(offset)).Limit(int(size)).Find(&list).Error; err != nil {
		return nil, 0, err
	}
	return list, total, nil
}
