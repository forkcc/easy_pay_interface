package service

import (
	"github.com/easypay/easy_pay_interface/internal/entity"
	"gorm.io/gorm"
)

// AddBalance 入账：增加商户账户余额并写流水（支付成功用正数，退款成功用负数）。事务内原子更新余额。
func AddBalance(db *gorm.DB, merchantID uint, amount int64, bizType, bizSn string) error {
	return db.Transaction(func(tx *gorm.DB) error {
		var a entity.Account
		err := tx.Where("merchant_id = ?", merchantID).First(&a).Error
		if err != nil {
			if err == gorm.ErrRecordNotFound {
				a = entity.Account{MerchantID: merchantID, Balance: 0, Frozen: 0}
				if errCreate := tx.Create(&a).Error; errCreate != nil {
					return errCreate
				}
			} else {
				return err
			}
		}
		if err := tx.Model(&entity.Account{}).Where("id = ?", a.ID).Update("balance", gorm.Expr("balance + ?", amount)).Error; err != nil {
			return err
		}
		var after entity.Account
		if err := tx.First(&after, a.ID).Error; err != nil {
			return err
		}
		return tx.Create(&entity.AccountFlow{
			AccountID:     a.ID,
			BizType:       bizType,
			BizSn:         bizSn,
			Amount:        amount,
			BalanceAfter:  after.Balance,
		}).Error
	})
}
