package repository

import (
	"github.com/easypay/easy_pay_interface/internal/entity"
	"gorm.io/gorm"
)

type ProfitShareRuleRepo struct{ db *gorm.DB }

func NewProfitShareRuleRepo(db *gorm.DB) *ProfitShareRuleRepo { return &ProfitShareRuleRepo{db: db} }

func (r *ProfitShareRuleRepo) GetByID(id uint) (*entity.ProfitShareRule, error) {
	var row entity.ProfitShareRule
	if err := r.db.First(&row, id).Error; err != nil {
		return nil, err
	}
	return &row, nil
}

func (r *ProfitShareRuleRepo) List(page, size int64, agentID, merchantID *uint) ([]entity.ProfitShareRule, int64, error) {
	q := r.db.Model(&entity.ProfitShareRule{})
	if agentID != nil {
		q = q.Where("agent_id = ?", *agentID)
	}
	if merchantID != nil {
		q = q.Where("merchant_id = ?", *merchantID)
	}
	var total int64
	if err := q.Count(&total).Error; err != nil {
		return nil, 0, err
	}
	var list []entity.ProfitShareRule
	offset := (page - 1) * size
	if err := q.Order("id DESC").Offset(int(offset)).Limit(int(size)).Find(&list).Error; err != nil {
		return nil, 0, err
	}
	return list, total, nil
}

func (r *ProfitShareRuleRepo) Create(row *entity.ProfitShareRule) error { return r.db.Create(row).Error }
func (r *ProfitShareRuleRepo) Update(row *entity.ProfitShareRule) error { return r.db.Save(row).Error }
func (r *ProfitShareRuleRepo) Delete(id uint) error {
	return r.db.Delete(&entity.ProfitShareRule{}, id).Error
}
