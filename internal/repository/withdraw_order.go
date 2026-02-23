package repository

import (
	"time"

	"github.com/easypay/easy_pay_interface/internal/entity"
	"gorm.io/gorm"
)

type WithdrawOrderRepo struct{ db *gorm.DB }

func NewWithdrawOrderRepo(db *gorm.DB) *WithdrawOrderRepo { return &WithdrawOrderRepo{db: db} }

func (r *WithdrawOrderRepo) GetByID(id uint) (*entity.WithdrawOrder, error) {
	var o entity.WithdrawOrder
	if err := r.db.First(&o, id).Error; err != nil {
		return nil, err
	}
	return &o, nil
}

func (r *WithdrawOrderRepo) ListByMerchantID(merchantID uint, page, size int64) ([]entity.WithdrawOrder, int64, error) {
	q := r.db.Model(&entity.WithdrawOrder{}).Where("merchant_id = ?", merchantID)
	var total int64
	if err := q.Count(&total).Error; err != nil {
		return nil, 0, err
	}
	var list []entity.WithdrawOrder
	offset := (page - 1) * size
	if err := q.Order("id DESC").Offset(int(offset)).Limit(int(size)).Find(&list).Error; err != nil {
		return nil, 0, err
	}
	return list, total, nil
}

func (r *WithdrawOrderRepo) List(page, size int64, merchantID *uint, status *int8) ([]entity.WithdrawOrder, int64, error) {
	q := r.db.Model(&entity.WithdrawOrder{})
	if merchantID != nil {
		q = q.Where("merchant_id = ?", *merchantID)
	}
	if status != nil {
		q = q.Where("status = ?", *status)
	}
	var total int64
	if err := q.Count(&total).Error; err != nil {
		return nil, 0, err
	}
	var list []entity.WithdrawOrder
	offset := (page - 1) * size
	if err := q.Order("id DESC").Offset(int(offset)).Limit(int(size)).Find(&list).Error; err != nil {
		return nil, 0, err
	}
	return list, total, nil
}

func (r *WithdrawOrderRepo) Create(o *entity.WithdrawOrder) error { return r.db.Create(o).Error }
func (r *WithdrawOrderRepo) Update(o *entity.WithdrawOrder) error { return r.db.Save(o).Error }

// ListByMerchantIDAndDate 按商户与日期范围（对账/统计用）
func (r *WithdrawOrderRepo) ListByMerchantIDAndDate(merchantID uint, start, end time.Time) ([]entity.WithdrawOrder, error) {
	var list []entity.WithdrawOrder
	err := r.db.Where("merchant_id = ? AND created_at >= ? AND created_at < ?", merchantID, start, end).
		Order("id ASC").Find(&list).Error
	return list, err
}
