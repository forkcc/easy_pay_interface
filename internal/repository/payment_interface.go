package repository

import (
	"github.com/easypay/easy_pay_interface/internal/entity"
	"gorm.io/gorm"
)

type PaymentInterfaceRepo struct{ db *gorm.DB }

func NewPaymentInterfaceRepo(db *gorm.DB) *PaymentInterfaceRepo { return &PaymentInterfaceRepo{db: db} }

func (r *PaymentInterfaceRepo) GetByID(id uint) (*entity.PaymentInterface, error) {
	var p entity.PaymentInterface
	if err := r.db.First(&p, id).Error; err != nil {
		return nil, err
	}
	return &p, nil
}

func (r *PaymentInterfaceRepo) GetByInterfaceCode(code string) (*entity.PaymentInterface, error) {
	var p entity.PaymentInterface
	if err := r.db.Where("interface_code = ?", code).First(&p).Error; err != nil {
		return nil, err
	}
	return &p, nil
}

func (r *PaymentInterfaceRepo) List(page, size int64, name string, status *int8) ([]entity.PaymentInterface, int64, error) {
	q := r.db.Model(&entity.PaymentInterface{})
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
	var list []entity.PaymentInterface
	offset := (page - 1) * size
	if err := q.Order("id DESC").Offset(int(offset)).Limit(int(size)).Find(&list).Error; err != nil {
		return nil, 0, err
	}
	return list, total, nil
}

func (r *PaymentInterfaceRepo) Create(p *entity.PaymentInterface) error { return r.db.Create(p).Error }
func (r *PaymentInterfaceRepo) Update(p *entity.PaymentInterface) error { return r.db.Save(p).Error }
