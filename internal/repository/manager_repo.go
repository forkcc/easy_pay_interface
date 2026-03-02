package repository

import (
	"github.com/easypay/easy_pay_interface/internal/db"
	"github.com/easypay/easy_pay_interface/internal/entity"
	"gorm.io/gorm"
)

// GetManagerByLoginName 按登录名查询管理端用户（用于登录校验）
func GetManagerByLoginName(loginName string) (*entity.ManagerUser, error) {
	if db.DB() == nil {
		return nil, gorm.ErrRecordNotFound
	}
	var u entity.ManagerUser
	err := db.DB().Table(u.TableName()).Where("login_name = ?", loginName).First(&u).Error
	if err != nil {
		return nil, err
	}
	return &u, nil
}

// GetManagerByID 按主键查询管理端用户
func GetManagerByID(id int64) (*entity.ManagerUser, error) {
	if db.DB() == nil {
		return nil, gorm.ErrRecordNotFound
	}
	var u entity.ManagerUser
	err := db.DB().Table(u.TableName()).Where("id = ?", id).First(&u).Error
	if err != nil {
		return nil, err
	}
	return &u, nil
}
