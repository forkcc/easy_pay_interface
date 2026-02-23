package repository

import (
	"github.com/easypay/easy_pay_interface/internal/entity"
	"gorm.io/gorm"
)

type AccountRepo struct{ db *gorm.DB }

func NewAccountRepo(db *gorm.DB) *AccountRepo { return &AccountRepo{db: db} }

func (r *AccountRepo) GetByMerchantID(merchantID uint) (*entity.Account, error) {
	var a entity.Account
	err := r.db.Where("merchant_id = ?", merchantID).First(&a).Error
	if err != nil {
		return nil, err
	}
	return &a, nil
}

func (r *AccountRepo) GetByID(id uint) (*entity.Account, error) {
	var a entity.Account
	if err := r.db.First(&a, id).Error; err != nil {
		return nil, err
	}
	return &a, nil
}

func (r *AccountRepo) Update(a *entity.Account) error { return r.db.Save(a).Error }

func (r *AccountRepo) Create(a *entity.Account) error { return r.db.Create(a).Error }
