package repository

import (
	"github.com/easypay/easy_pay_interface/internal/entity"
	"gorm.io/gorm"
)

type PaymentProductRepo struct{ db *gorm.DB }

func NewPaymentProductRepo(db *gorm.DB) *PaymentProductRepo { return &PaymentProductRepo{db: db} }

func (r *PaymentProductRepo) GetByID(id uint) (*entity.PaymentProduct, error) {
	var p entity.PaymentProduct
	if err := r.db.First(&p, id).Error; err != nil {
		return nil, err
	}
	return &p, nil
}

func (r *PaymentProductRepo) GetByProductCode(code string) (*entity.PaymentProduct, error) {
	var p entity.PaymentProduct
	if err := r.db.Where("product_code = ?", code).First(&p).Error; err != nil {
		return nil, err
	}
	return &p, nil
}

func (r *PaymentProductRepo) List(page, size int64, name string, status *int8) ([]entity.PaymentProduct, int64, error) {
	q := r.db.Model(&entity.PaymentProduct{})
	if name != "" {
		q = q.Where("name LIKE ?", "%"+name+"%")
	}
	if status != nil {
		q = q.Where("status = ?", *status)
	}
	var total int64
	if err := q.Count(&total).Error; err != nil {
		return nil, 0, err
	}
	var list []entity.PaymentProduct
	offset := (page - 1) * size
	if err := q.Order("id DESC").Offset(int(offset)).Limit(int(size)).Find(&list).Error; err != nil {
		return nil, 0, err
	}
	return list, total, nil
}

func (r *PaymentProductRepo) Create(p *entity.PaymentProduct) error { return r.db.Create(p).Error }
func (r *PaymentProductRepo) Update(p *entity.PaymentProduct) error { return r.db.Save(p).Error }
