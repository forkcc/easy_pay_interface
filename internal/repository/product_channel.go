package repository

import (
	"github.com/easypay/easy_pay_interface/internal/entity"
	"gorm.io/gorm"
)

type ProductChannelRepo struct{ db *gorm.DB }

func NewProductChannelRepo(db *gorm.DB) *ProductChannelRepo { return &ProductChannelRepo{db: db} }

func (r *ProductChannelRepo) ListByProductID(productID uint) ([]entity.ProductChannel, error) {
	var list []entity.ProductChannel
	if err := r.db.Where("product_id = ?", productID).Order("sort_order ASC, id ASC").Find(&list).Error; err != nil {
		return nil, err
	}
	return list, nil
}

func (r *ProductChannelRepo) Create(pc *entity.ProductChannel) error { return r.db.Create(pc).Error }

func (r *ProductChannelRepo) DeleteByProductAndChannel(productID, channelID uint) error {
	return r.db.Where("product_id = ? AND channel_id = ?", productID, channelID).Delete(&entity.ProductChannel{}).Error
}

func (r *ProductChannelRepo) UpdateSortOrder(productID, channelID uint, sortOrder int) error {
	return r.db.Model(&entity.ProductChannel{}).Where("product_id = ? AND channel_id = ?", productID, channelID).Update("sort_order", sortOrder).Error
}

func (r *ProductChannelRepo) GetByProductAndChannel(productID, channelID uint) (*entity.ProductChannel, error) {
	var pc entity.ProductChannel
	if err := r.db.Where("product_id = ? AND channel_id = ?", productID, channelID).First(&pc).Error; err != nil {
		return nil, err
	}
	return &pc, nil
}
