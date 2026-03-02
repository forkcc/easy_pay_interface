package repository

import (
	"github.com/easypay/easy_pay_interface/internal/db"
	"github.com/easypay/easy_pay_interface/internal/entity"
	"gorm.io/gorm"
)

// GetMerchantByMchNo 按商户号查询
func GetMerchantByMchNo(mchNo string) (*entity.Merchant, error) {
	if db.DB() == nil {
		return nil, gorm.ErrRecordNotFound
	}
	var m entity.Merchant
	err := db.DB().Table(m.TableName()).Where("mch_no = ?", mchNo).First(&m).Error
	if err != nil {
		return nil, err
	}
	return &m, nil
}

// GetMerchantByLoginName 按登录名查询
func GetMerchantByLoginName(loginName string) (*entity.Merchant, error) {
	if db.DB() == nil {
		return nil, gorm.ErrRecordNotFound
	}
	var m entity.Merchant
	err := db.DB().Table(m.TableName()).Where("login_name = ?", loginName).First(&m).Error
	if err != nil {
		return nil, err
	}
	return &m, nil
}
