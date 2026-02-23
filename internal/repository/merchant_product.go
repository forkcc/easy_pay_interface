package repository

import (
	"github.com/easypay/easy_pay_interface/internal/entity"
	"gorm.io/gorm"
)

type MerchantProductRepo struct{ db *gorm.DB }

func NewMerchantProductRepo(db *gorm.DB) *MerchantProductRepo { return &MerchantProductRepo{db: db} }

func (r *MerchantProductRepo) ListProductIDsByMerchantID(merchantID uint) ([]uint, error) {
	var list []entity.MerchantProduct
	if err := r.db.Where("merchant_id = ?", merchantID).Find(&list).Error; err != nil {
		return nil, err
	}
	ids := make([]uint, 0, len(list))
	for _, mp := range list {
		ids = append(ids, mp.ProductID)
	}
	return ids, nil
}

func (r *MerchantProductRepo) Create(mp *entity.MerchantProduct) error { return r.db.Create(mp).Error }
func (r *MerchantProductRepo) DeleteByMerchantAndProduct(merchantID, productID uint) error {
	return r.db.Where("merchant_id = ? AND product_id = ?", merchantID, productID).Delete(&entity.MerchantProduct{}).Error
}
