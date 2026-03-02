package repository

import (
	"github.com/easypay/easy_pay_interface/internal/db"
	"github.com/easypay/easy_pay_interface/internal/entity"
	"gorm.io/gorm"
)

// GetAgentByAgentNo 按代理号查询
func GetAgentByAgentNo(agentNo string) (*entity.Agent, error) {
	if db.DB() == nil {
		return nil, gorm.ErrRecordNotFound
	}
	var a entity.Agent
	err := db.DB().Table(a.TableName()).Where("agent_no = ?", agentNo).First(&a).Error
	if err != nil {
		return nil, err
	}
	return &a, nil
}

// GetAgentByLoginName 按登录名查询
func GetAgentByLoginName(loginName string) (*entity.Agent, error) {
	if db.DB() == nil {
		return nil, gorm.ErrRecordNotFound
	}
	var a entity.Agent
	err := db.DB().Table(a.TableName()).Where("login_name = ?", loginName).First(&a).Error
	if err != nil {
		return nil, err
	}
	return &a, nil
}
