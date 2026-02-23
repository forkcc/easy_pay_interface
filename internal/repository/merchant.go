package repository

import (
	"github.com/easypay/easy_pay_interface/internal/entity"
	"gorm.io/gorm"
)

type MerchantRepo struct{ db *gorm.DB }

func NewMerchantRepo(db *gorm.DB) *MerchantRepo { return &MerchantRepo{db: db} }

func (r *MerchantRepo) GetByID(id uint) (*entity.Merchant, error) {
	var m entity.Merchant
	err := r.db.First(&m, id).Error
	if err != nil {
		return nil, err
	}
	return &m, nil
}

func (r *MerchantRepo) GetByMerchantNo(no string) (*entity.Merchant, error) {
	var m entity.Merchant
	err := r.db.Where("merchant_no = ?", no).First(&m).Error
	if err != nil {
		return nil, err
	}
	return &m, nil
}

func (r *MerchantRepo) GetByApiKey(apiKey string) (*entity.Merchant, error) {
	if apiKey == "" {
		return nil, gorm.ErrRecordNotFound
	}
	var m entity.Merchant
	err := r.db.Where("api_key = ?", apiKey).First(&m).Error
	if err != nil {
		return nil, err
	}
	return &m, nil
}

func (r *MerchantRepo) List(page, size int64, name string, status *int8) ([]entity.Merchant, int64, error) {
	var list []entity.Merchant
	q := r.db.Model(&entity.Merchant{})
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
	offset := (page - 1) * size
	if err := q.Order("id DESC").Offset(int(offset)).Limit(int(size)).Find(&list).Error; err != nil {
		return nil, 0, err
	}
	return list, total, nil
}

func (r *MerchantRepo) Create(m *entity.Merchant) error { return r.db.Create(m).Error }
func (r *MerchantRepo) Update(m *entity.Merchant) error { return r.db.Save(m).Error }
