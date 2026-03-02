package repository

import (
	"github.com/easypay/easy_pay_interface/internal/db"
	"github.com/easypay/easy_pay_interface/internal/entity"
	"gorm.io/gorm"
)

// GetPayChannel 按商户+应用+方式查渠道
func GetPayChannel(mchNo, appId, wayCode string) (*entity.PayChannel, error) {
	if db.DB() == nil {
		return nil, gorm.ErrRecordNotFound
	}
	var ch entity.PayChannel
	err := db.DB().Table(ch.TableName()).Where("mch_no = ? AND app_id = ? AND way_code = ?", mchNo, appId, wayCode).First(&ch).Error
	if err != nil {
		return nil, err
	}
	return &ch, nil
}

// GetPayOrderByNo 按支付订单号查询
func GetPayOrderByNo(payOrderNo string) (*entity.PayOrder, error) {
	if db.DB() == nil {
		return nil, gorm.ErrRecordNotFound
	}
	var order entity.PayOrder
	err := db.DB().Table(order.TableName()).Where("pay_order_no = ?", payOrderNo).First(&order).Error
	if err != nil {
		return nil, err
	}
	return &order, nil
}

// GetPayOrderByNoAndMch 按支付订单号与商户号查询
func GetPayOrderByNoAndMch(payOrderNo, mchNo string) (*entity.PayOrder, error) {
	if db.DB() == nil {
		return nil, gorm.ErrRecordNotFound
	}
	var order entity.PayOrder
	err := db.DB().Table(order.TableName()).Where("pay_order_no = ? AND mch_no = ?", payOrderNo, mchNo).First(&order).Error
	if err != nil {
		return nil, err
	}
	return &order, nil
}

// CreatePayOrder 创建支付订单
func CreatePayOrder(order *entity.PayOrder) error {
	if db.DB() == nil {
		return gorm.ErrRecordNotFound
	}
	return db.DB().Table(order.TableName()).Create(order).Error
}

// CreateOrderRecord 创建订单状态记录
func CreateOrderRecord(record *entity.OrderRecord) error {
	if db.DB() == nil {
		return gorm.ErrRecordNotFound
	}
	return db.DB().Table(record.TableName()).Create(record).Error
}

// ClosePayOrder 关闭待支付订单，返回影响行数
func ClosePayOrder(payOrderNo string, cancelTime, updatedAt int64) (int64, error) {
	if db.DB() == nil {
		return 0, gorm.ErrRecordNotFound
	}
	res := db.DB().Table((&entity.PayOrder{}).TableName()).
		Where("pay_order_no = ? AND state = ?", payOrderNo, 0).
		Updates(map[string]interface{}{
			"state":       2,
			"cancel_time": cancelTime,
			"updated_at":  updatedAt,
		})
	return res.RowsAffected, res.Error
}

// CreateRefundOrder 创建退款订单
func CreateRefundOrder(refund *entity.RefundOrder) error {
	if db.DB() == nil {
		return gorm.ErrRecordNotFound
	}
	return db.DB().Table(refund.TableName()).Create(refund).Error
}
