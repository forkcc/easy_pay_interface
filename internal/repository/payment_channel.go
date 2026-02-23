package repository

import (
	"github.com/easypay/easy_pay_interface/internal/entity"
	"gorm.io/gorm"
)

type PaymentChannelRepo struct{ db *gorm.DB }

func NewPaymentChannelRepo(db *gorm.DB) *PaymentChannelRepo { return &PaymentChannelRepo{db: db} }

func (r *PaymentChannelRepo) GetByID(id uint) (*entity.PaymentChannel, error) {
	var c entity.PaymentChannel
	if err := r.db.First(&c, id).Error; err != nil {
		return nil, err
	}
	return &c, nil
}

func (r *PaymentChannelRepo) GetByChannelCode(code string) (*entity.PaymentChannel, error) {
	var c entity.PaymentChannel
	if err := r.db.Where("channel_code = ?", code).First(&c).Error; err != nil {
		return nil, err
	}
	return &c, nil
}

func (r *PaymentChannelRepo) List(page, size int64, interfaceID *uint, status *int8) ([]entity.PaymentChannel, int64, error) {
	q := r.db.Model(&entity.PaymentChannel{})
	if interfaceID != nil {
		q = q.Where("interface_id = ?", *interfaceID)
	}
	if status != nil {
		q = q.Where("status = ?", *status)
	}
	var total int64
	if err := q.Count(&total).Error; err != nil {
		return nil, 0, err
	}
	var list []entity.PaymentChannel
	offset := (page - 1) * size
	if err := q.Order("id DESC").Offset(int(offset)).Limit(int(size)).Find(&list).Error; err != nil {
		return nil, 0, err
	}
	return list, total, nil
}

func (r *PaymentChannelRepo) Create(c *entity.PaymentChannel) error { return r.db.Create(c).Error }
func (r *PaymentChannelRepo) Update(c *entity.PaymentChannel) error { return r.db.Save(c).Error }
