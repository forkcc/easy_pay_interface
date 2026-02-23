package repository

import (
	"github.com/easypay/easy_pay_interface/internal/entity"
	"gorm.io/gorm"
)

type AgentRepo struct{ db *gorm.DB }

func NewAgentRepo(db *gorm.DB) *AgentRepo { return &AgentRepo{db: db} }

func (r *AgentRepo) GetByID(id uint) (*entity.Agent, error) {
	var a entity.Agent
	err := r.db.First(&a, id).Error
	if err != nil {
		return nil, err
	}
	return &a, nil
}

func (r *AgentRepo) GetByAgentNo(no string) (*entity.Agent, error) {
	var a entity.Agent
	if err := r.db.Where("agent_no = ?", no).First(&a).Error; err != nil {
		return nil, err
	}
	return &a, nil
}

func (r *AgentRepo) GetByApiKey(apiKey string) (*entity.Agent, error) {
	if apiKey == "" {
		return nil, gorm.ErrRecordNotFound
	}
	var a entity.Agent
	if err := r.db.Where("api_key = ?", apiKey).First(&a).Error; err != nil {
		return nil, err
	}
	return &a, nil
}

func (r *AgentRepo) List(page, size int64) ([]entity.Agent, int64, error) {
	var list []entity.Agent
	q := r.db.Model(&entity.Agent{})
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

func (r *AgentRepo) Create(a *entity.Agent) error { return r.db.Create(a).Error }
func (r *AgentRepo) Update(a *entity.Agent) error { return r.db.Save(a).Error }
