package repository

import (
	"time"

	"github.com/easypay/easy_pay_interface/internal/entity"
	"gorm.io/gorm"
)

type PayOrderRepo struct{ db *gorm.DB }

func NewPayOrderRepo(db *gorm.DB) *PayOrderRepo { return &PayOrderRepo{db: db} }

func (r *PayOrderRepo) GetByPayOrderNo(no string) (*entity.PayOrder, error) {
	var o entity.PayOrder
	err := r.db.Where("pay_order_no = ?", no).First(&o).Error
	if err != nil {
		return nil, err
	}
	return &o, nil
}

func (r *PayOrderRepo) GetByID(id uint) (*entity.PayOrder, error) {
	var o entity.PayOrder
	if err := r.db.First(&o, id).Error; err != nil {
		return nil, err
	}
	return &o, nil
}

func (r *PayOrderRepo) GetByMerchantIDAndOutTradeNo(merchantID uint, outTradeNo string) (*entity.PayOrder, error) {
	var o entity.PayOrder
	err := r.db.Where("merchant_id = ? AND out_trade_no = ?", merchantID, outTradeNo).First(&o).Error
	if err != nil {
		return nil, err
	}
	return &o, nil
}

func (r *PayOrderRepo) ListByMerchantID(merchantID uint, page, size int64) ([]entity.PayOrder, int64, error) {
	var list []entity.PayOrder
	q := r.db.Model(&entity.PayOrder{}).Where("merchant_id = ?", merchantID)
	var total int64
	if err := q.Count(&total).Error; err != nil {
		return nil, 0, err
	}
	offset := (page - 1) * size
	if err := q.Order("id DESC").Offset(int(offset)).Limit(int(size)).Find(&list).Error; err != nil {
		return nil, 0, err
	}
	return list, total, nil
}

func (r *PayOrderRepo) Create(o *entity.PayOrder) error { return r.db.Create(o).Error }
func (r *PayOrderRepo) Update(o *entity.PayOrder) error { return r.db.Save(o).Error }

// ListByMerchantIDAndDate 按商户与日期范围查询支付订单（对账用，按创建时间），end 为次日 0 点（不包含）
func (r *PayOrderRepo) ListByMerchantIDAndDate(merchantID uint, start, end time.Time) ([]entity.PayOrder, error) {
	return r.listByMerchantIDAndDateWithTime(merchantID, start, end, true)
}

// ListByMerchantIDAndDateByUpdated 按商户与日期范围查询支付订单（对账用，按更新时间）
func (r *PayOrderRepo) ListByMerchantIDAndDateByUpdated(merchantID uint, start, end time.Time) ([]entity.PayOrder, error) {
	return r.listByMerchantIDAndDateWithTime(merchantID, start, end, false)
}

func (r *PayOrderRepo) listByMerchantIDAndDateWithTime(merchantID uint, start, end time.Time, byCreated bool) ([]entity.PayOrder, error) {
	var list []entity.PayOrder
	timeCol := "created_at"
	if !byCreated {
		timeCol = "updated_at"
	}
	err := r.db.Where("merchant_id = ? AND "+timeCol+" >= ? AND "+timeCol+" < ?", merchantID, start, end).
		Order("id ASC").Find(&list).Error
	return list, err
}

// SumSuccessByDate 按日期（创建时间）汇总支付成功笔数与金额（status=1），按 merchant_id 分组，对账用
func (r *PayOrderRepo) SumSuccessByDate(start, end time.Time) ([]struct {
	MerchantID uint
	Count      int64
	Amount     int64
}, error) {
	return r.sumSuccessByDateWithTime(start, end, true)
}

// SumSuccessByDateByUpdated 按日期（更新时间）汇总支付成功笔数与金额
func (r *PayOrderRepo) SumSuccessByDateByUpdated(start, end time.Time) ([]struct {
	MerchantID uint
	Count      int64
	Amount     int64
}, error) {
	return r.sumSuccessByDateWithTime(start, end, false)
}

func (r *PayOrderRepo) sumSuccessByDateWithTime(start, end time.Time, byCreated bool) ([]struct {
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
	err := r.db.Model(&entity.PayOrder{}).Where(timeCol+" >= ? AND "+timeCol+" < ? AND status = ?", start, end, 1).
		Select("merchant_id as merchant_id, count(*) as count, coalesce(sum(amount),0) as amount").Group("merchant_id").Scan(&result).Error
	return result, err
}
