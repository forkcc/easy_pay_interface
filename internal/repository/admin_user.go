package repository

import (
	"github.com/easypay/easy_pay_interface/internal/entity"
	"gorm.io/gorm"
)

type AdminUserRepo struct{ db *gorm.DB }

func NewAdminUserRepo(db *gorm.DB) *AdminUserRepo { return &AdminUserRepo{db: db} }

func (r *AdminUserRepo) GetByID(id uint) (*entity.AdminUser, error) {
	var u entity.AdminUser
	if err := r.db.First(&u, id).Error; err != nil {
		return nil, err
	}
	return &u, nil
}

func (r *AdminUserRepo) GetByUsername(username string) (*entity.AdminUser, error) {
	var u entity.AdminUser
	if err := r.db.Where("username = ? AND status = 1", username).First(&u).Error; err != nil {
		return nil, err
	}
	return &u, nil
}

func (r *AdminUserRepo) Create(u *entity.AdminUser) error { return r.db.Create(u).Error }
func (r *AdminUserRepo) Save(u *entity.AdminUser) error   { return r.db.Save(u).Error }

func (r *AdminUserRepo) Count() (int64, error) {
	var n int64
	err := r.db.Model(&entity.AdminUser{}).Count(&n).Error
	return n, err
}
