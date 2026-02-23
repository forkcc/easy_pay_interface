package repository

import (
	"time"

	"github.com/easypay/easy_pay_interface/internal/entity"
	"gorm.io/gorm"
)

type ReconcileReportRepo struct{ db *gorm.DB }

func NewReconcileReportRepo(db *gorm.DB) *ReconcileReportRepo { return &ReconcileReportRepo{db: db} }

func (r *ReconcileReportRepo) Save(row *entity.ReconcileReport) error {
	return r.db.Save(row).Error
}

func (r *ReconcileReportRepo) GetByDateAndMerchant(reconcileDate time.Time, merchantID uint) (*entity.ReconcileReport, error) {
	return r.GetByDateAndMerchantAndTimeType(reconcileDate, merchantID, entity.ReconcileTimeTypeCreated)
}

// GetByDateAndMerchantAndTimeType 按日期、商户、时间类型取一条
func (r *ReconcileReportRepo) GetByDateAndMerchantAndTimeType(reconcileDate time.Time, merchantID uint, timeType string) (*entity.ReconcileReport, error) {
	var row entity.ReconcileReport
	d := reconcileDate.Format("2006-01-02")
	err := r.db.Where("reconcile_date = ? AND merchant_id = ? AND time_type = ?", d, merchantID, timeType).First(&row).Error
	if err != nil {
		return nil, err
	}
	return &row, nil
}

func (r *ReconcileReportRepo) CountByDate(reconcileDate time.Time) (int64, error) {
	var n int64
	d := reconcileDate.Format("2006-01-02")
	err := r.db.Model(&entity.ReconcileReport{}).Where("reconcile_date = ?", d).Count(&n).Error
	return n, err
}

func (r *ReconcileReportRepo) ListByDate(reconcileDate time.Time) ([]entity.ReconcileReport, error) {
	var list []entity.ReconcileReport
	d := reconcileDate.Format("2006-01-02")
	err := r.db.Where("reconcile_date = ?", d).Order("merchant_id ASC, time_type ASC").Find(&list).Error
	return list, err
}
