package repository

import (
	"github.com/easypay/easy_pay_interface/internal/entity"
	"gorm.io/gorm"
)

type AgentUserRepo struct{ db *gorm.DB }

func NewAgentUserRepo(db *gorm.DB) *AgentUserRepo { return &AgentUserRepo{db: db} }

func (r *AgentUserRepo) GetByID(id uint) (*entity.AgentUser, error) {
	var u entity.AgentUser
	if err := r.db.First(&u, id).Error; err != nil {
		return nil, err
	}
	return &u, nil
}

func (r *AgentUserRepo) GetByAgentIDAndUsername(agentID uint, username string) (*entity.AgentUser, error) {
	var u entity.AgentUser
	if err := r.db.Where("agent_id = ? AND username = ? AND status = 1", agentID, username).First(&u).Error; err != nil {
		return nil, err
	}
	return &u, nil
}

func (r *AgentUserRepo) Create(u *entity.AgentUser) error { return r.db.Create(u).Error }
func (r *AgentUserRepo) Save(u *entity.AgentUser) error   { return r.db.Save(u).Error }
