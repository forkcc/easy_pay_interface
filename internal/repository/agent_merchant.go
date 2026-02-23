package repository

import (
	"github.com/easypay/easy_pay_interface/internal/entity"
	"gorm.io/gorm"
)

type AgentMerchantRepo struct{ db *gorm.DB }

func NewAgentMerchantRepo(db *gorm.DB) *AgentMerchantRepo { return &AgentMerchantRepo{db: db} }

func (r *AgentMerchantRepo) PageMerchantIDsByAgentID(agentID uint, page, size int64) ([]uint, int64, error) {
	q := r.db.Model(&entity.AgentMerchant{}).Where("agent_id = ?", agentID)
	var total int64
	if err := q.Count(&total).Error; err != nil {
		return nil, 0, err
	}
	var list []entity.AgentMerchant
	offset := (page - 1) * size
	if err := q.Order("id DESC").Offset(int(offset)).Limit(int(size)).Find(&list).Error; err != nil {
		return nil, 0, err
	}
	ids := make([]uint, 0, len(list))
	for _, am := range list {
		ids = append(ids, am.MerchantID)
	}
	return ids, total, nil
}
