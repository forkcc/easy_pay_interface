package repository

import (
	"fmt"

	"github.com/easypay/easy_pay_interface/internal/entity"
	"gorm.io/gorm"
)

type BotChatBindRepo struct{ db *gorm.DB }

func NewBotChatBindRepo(db *gorm.DB) *BotChatBindRepo { return &BotChatBindRepo{db: db} }

func (r *BotChatBindRepo) GetByChatID(chatID int64) (*entity.BotChatBind, error) {
	var b entity.BotChatBind
	if err := r.db.Where("chat_id = ?", chatID).First(&b).Error; err != nil {
		return nil, err
	}
	return &b, nil
}

func (r *BotChatBindRepo) GetChatIDByMerchantID(merchantID uint) (int64, error) {
	var b entity.BotChatBind
	if err := r.db.Where("bind_type = ? AND bind_id = ?", entity.BotBindTypeMerchant, fmt.Sprintf("%d", merchantID)).First(&b).Error; err != nil {
		return 0, err
	}
	return b.ChatID, nil
}

// GetChatIDByChannel 渠道群：按 interface_code 查 chat_id
func (r *BotChatBindRepo) GetChatIDByChannel(interfaceCode string) (int64, error) {
	var b entity.BotChatBind
	if err := r.db.Where("bind_type = ? AND bind_id = ?", entity.BotBindTypeChannel, interfaceCode).First(&b).Error; err != nil {
		return 0, err
	}
	return b.ChatID, nil
}

func (r *BotChatBindRepo) Save(b *entity.BotChatBind) error {
	return r.db.Save(b).Error
}

func (r *BotChatBindRepo) Create(b *entity.BotChatBind) error { return r.db.Create(b).Error }
func (r *BotChatBindRepo) ListAllChatIDs() ([]int64, error) {
	var ids []int64
	if err := r.db.Model(&entity.BotChatBind{}).Distinct("chat_id").Pluck("chat_id", &ids).Error; err != nil {
		return nil, err
	}
	return ids, nil
}
func (r *BotChatBindRepo) DeleteByChatID(chatID int64) error {
	return r.db.Where("chat_id = ?", chatID).Delete(&entity.BotChatBind{}).Error
}
