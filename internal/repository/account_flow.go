package repository

import (
	"time"

	"github.com/easypay/easy_pay_interface/internal/entity"
	"gorm.io/gorm"
)

type AccountFlowRepo struct{ db *gorm.DB }

func NewAccountFlowRepo(db *gorm.DB) *AccountFlowRepo { return &AccountFlowRepo{db: db} }

func (r *AccountFlowRepo) Create(f *entity.AccountFlow) error { return r.db.Create(f).Error }

// ListByAccountIDAndDate 按账户与日期范围查询流水（对账用，按创建时间），end 为次日 0 点（不包含）
func (r *AccountFlowRepo) ListByAccountIDAndDate(accountID uint, start, end time.Time) ([]entity.AccountFlow, error) {
	return r.listByAccountIDAndDateWithTime(accountID, start, end, true)
}

// ListByAccountIDAndDateByUpdated 按账户与日期范围查询流水（按更新时间）
func (r *AccountFlowRepo) ListByAccountIDAndDateByUpdated(accountID uint, start, end time.Time) ([]entity.AccountFlow, error) {
	return r.listByAccountIDAndDateWithTime(accountID, start, end, false)
}

func (r *AccountFlowRepo) listByAccountIDAndDateWithTime(accountID uint, start, end time.Time, byCreated bool) ([]entity.AccountFlow, error) {
	var list []entity.AccountFlow
	timeCol := "created_at"
	if !byCreated {
		timeCol = "updated_at"
	}
	err := r.db.Where("account_id = ? AND "+timeCol+" >= ? AND "+timeCol+" < ?", accountID, start, end).
		Order("id ASC").Find(&list).Error
	return list, err
}
