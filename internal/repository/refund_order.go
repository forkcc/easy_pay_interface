package repository

import (
	"time"

	"github.com/easypay/easy_pay_interface/internal/entity"
	"gorm.io/gorm"
)

type RefundOrderRepo struct{ db *gorm.DB }

func NewRefundOrderRepo(db *gorm.DB) *RefundOrderRepo { return &RefundOrderRepo{db: db} }

func (r *RefundOrderRepo) GetByRefundOrderNo(no string) (*entity.RefundOrder, error) {
	var o entity.RefundOrder
	if err := r.db.Where("refund_order_no = ?", no).First(&o).Error; err != nil {
		return nil, err
	}
	return &o, nil
}

func (r *RefundOrderRepo) GetByMerchantIDAndOutRefundNo(merchantID uint, outRefundNo string) (*entity.RefundOrder, error) {
	var o entity.RefundOrder
	if err := r.db.Where("merchant_id = ? AND out_refund_no = ?", merchantID, outRefundNo).First(&o).Error; err != nil {
		return nil, err
	}
	return &o, nil
}

func (r *RefundOrderRepo) Update(o *entity.RefundOrder) error { return r.db.Save(o).Error }

func (r *RefundOrderRepo) Create(o *entity.RefundOrder) error { return r.db.Create(o).Error }

// ListByMerchantIDAndDate 按商户与日期范围查询退款订单（对账用，按创建时间），end 为次日 0 点（不包含）
func (r *RefundOrderRepo) ListByMerchantIDAndDate(merchantID uint, start, end time.Time) ([]entity.RefundOrder, error) {
	return r.listByMerchantIDAndDateWithTime(merchantID, start, end, true)
}

// ListByMerchantIDAndDateByUpdated 按商户与日期范围查询退款订单（对账用，按更新时间）
func (r *RefundOrderRepo) ListByMerchantIDAndDateByUpdated(merchantID uint, start, end time.Time) ([]entity.RefundOrder, error) {
	return r.listByMerchantIDAndDateWithTime(merchantID, start, end, false)
}

func (r *RefundOrderRepo) listByMerchantIDAndDateWithTime(merchantID uint, start, end time.Time, byCreated bool) ([]entity.RefundOrder, error) {
	var list []entity.RefundOrder
	timeCol := "created_at"
	if !byCreated {
		timeCol = "updated_at"
	}
	err := r.db.Where("merchant_id = ? AND "+timeCol+" >= ? AND "+timeCol+" < ?", merchantID, start, end).
		Order("id ASC").Find(&list).Error
	return list, err
}

// SumSuccessByDate 按日期（创建时间）汇总退款成功笔数与金额（status=1），按 merchant_id 分组，对账用
func (r *RefundOrderRepo) SumSuccessByDate(start, end time.Time) ([]struct {
	MerchantID uint
	Count      int64
	Amount     int64
}, error) {
	return r.sumSuccessByDateWithTime(start, end, true)
}

// SumSuccessByDateByUpdated 按日期（更新时间）汇总退款成功笔数与金额
func (r *RefundOrderRepo) SumSuccessByDateByUpdated(start, end time.Time) ([]struct {
	MerchantID uint
	Count      int64
	Amount     int64
}, error) {
	return r.sumSuccessByDateWithTime(start, end, false)
}

func (r *RefundOrderRepo) sumSuccessByDateWithTime(start, end time.Time, byCreated bool) ([]struct {
	MerchantID uint
	Count      int64
	Amount     int64
}, error) {
	var result []struct {
		MerchantID uint
		Count      int64
		Amount     int64
	}
	timeCol := "created_at"
	if !byCreated {
		timeCol = "updated_at"
	}
	err := r.db.Model(&entity.RefundOrder{}).Where(timeCol+" >= ? AND "+timeCol+" < ? AND status = ?", start, end, 1).
		Select("merchant_id as merchant_id, count(*) as count, coalesce(sum(amount),0) as amount").Group("merchant_id").Scan(&result).Error
	return result, err
}
