package repository

import (
	"github.com/easypay/easy_pay_interface/internal/entity"
	"gorm.io/gorm"
)

type NotifyLogRepo struct{ db *gorm.DB }

func NewNotifyLogRepo(db *gorm.DB) *NotifyLogRepo { return &NotifyLogRepo{db: db} }

func (r *NotifyLogRepo) Create(n *entity.NotifyLog) error { return r.db.Create(n).Error }

func (r *NotifyLogRepo) Update(n *entity.NotifyLog) error { return r.db.Save(n).Error }

// ListPending 查询未成功且重试次数小于 maxRetry 的记录（resp_status 非 200 或为空）
func (r *NotifyLogRepo) ListPending(maxRetry int, limit int) ([]entity.NotifyLog, error) {
	var list []entity.NotifyLog
	q := r.db.Where("retry_count < ?", maxRetry).Where("(resp_status IS NULL OR resp_status != 200)")
	if err := q.Order("id ASC").Limit(limit).Find(&list).Error; err != nil {
		return nil, err
	}
	return list, nil
}

func (r *NotifyLogRepo) GetByID(id uint) (*entity.NotifyLog, error) {
	var n entity.NotifyLog
	if err := r.db.First(&n, id).Error; err != nil {
		return nil, err
	}
	return &n, nil
}
