package rpc

import (
	"context"
	"fmt"
	"time"

	"github.com/easypay/easy_pay_interface/internal/db"
	"github.com/easypay/easy_pay_interface/internal/entity"
	"github.com/easypay/easy_pay_interface/internal/repository"
	"gorm.io/gorm"
)

// PaymentProvider 支付模块 Dubbo 接口（参考 Jeepay 支付网关 / 龙果支付网关）
type PaymentProvider struct{}

func (p *PaymentProvider) Reference() string { return "PaymentProvider" }

// UnifiedOrder 统一下单（仿 Jeepay 支付接口）
func (p *PaymentProvider) UnifiedOrder(ctx context.Context, req *UnifiedOrderReq) (*UnifiedOrderResp, error) {
	if db.DB() == nil {
		return nil, errDBNotInit
	}
	if req == nil || req.MchNo == "" || req.AppId == "" || req.MchOrderNo == "" || req.WayCode == "" || req.Amount <= 0 {
		return nil, fmt.Errorf("invalid unified order request")
	}
	if req.Currency == "" {
		req.Currency = "CNY"
	}
	now := time.Now().UnixMilli()
	payOrderNo := fmt.Sprintf("P%d%06d", now, time.Now().Nanosecond()%1000000)
	expireSec := req.ExpiredSec
	if expireSec <= 0 {
		expireSec = 7200
	}
	expireTime := now + expireSec*1000
	ifCode := ""
	if ch, err := repository.GetPayChannel(req.MchNo, req.AppId, req.WayCode); err == nil {
		ifCode = ch.IfCode
	}
	order := &entity.PayOrder{
		PayOrderNo: payOrderNo,
		MchNo:      req.MchNo,
		AppID:      req.AppId,
		IfCode:     ifCode,
		WayCode:    req.WayCode,
		Amount:     req.Amount,
		Currency:   req.Currency,
		State:      0,
		ExpireTime: &expireTime,
		CreatedAt:  now,
		UpdatedAt:  now,
	}
	if req.Subject != "" {
		order.Subject = &req.Subject
	}
	if req.Body != "" {
		order.Body = &req.Body
	}
	if req.NotifyUrl != "" {
		order.NotifyURL = &req.NotifyUrl
	}
	if req.ReturnUrl != "" {
		order.ReturnURL = &req.ReturnUrl
	}
	if err := repository.CreatePayOrder(order); err != nil {
		return nil, err
	}
	_ = repository.CreateOrderRecord(&entity.OrderRecord{
		PayOrderNo: payOrderNo,
		EventType:  "create",
		NewState:   ptrInt16(0),
		CreatedAt:  now,
	})
	return &UnifiedOrderResp{
		PayOrderNo: payOrderNo,
		State:      0,
		CodeUrl:    fmt.Sprintf("https://pay.example.com/qr/%s", payOrderNo),
	}, nil
}

// QueryOrder 查询支付订单（仿 Jeepay 订单查询）
func (p *PaymentProvider) QueryOrder(ctx context.Context, payOrderNo string) (*entity.PayOrder, error) {
	return repository.GetPayOrderByNo(payOrderNo)
}

// Refund 退款（仿 Jeepay 退款接口）
func (p *PaymentProvider) Refund(ctx context.Context, req *RefundReq) (*entity.RefundOrder, error) {
	if db.DB() == nil {
		return nil, errDBNotInit
	}
	if req == nil || req.RefundOrderNo == "" || req.PayOrderNo == "" || req.MchNo == "" || req.RefundAmount <= 0 {
		return nil, fmt.Errorf("invalid refund request")
	}
	payOrder, err := repository.GetPayOrderByNoAndMch(req.PayOrderNo, req.MchNo)
	if err != nil {
		return nil, err
	}
	now := time.Now().UnixMilli()
	refund := &entity.RefundOrder{
		RefundOrderNo: req.RefundOrderNo,
		PayOrderNo:    req.PayOrderNo,
		MchNo:         req.MchNo,
		AppID:         payOrder.AppID,
		RefundAmount:  req.RefundAmount,
		Currency:      "CNY",
		State:         0,
		ApplyTime:     now,
		CreatedAt:     now,
		UpdatedAt:     now,
	}
	if req.Reason != "" {
		refund.Reason = &req.Reason
	}
	if err := repository.CreateRefundOrder(refund); err != nil {
		return nil, err
	}
	return refund, nil
}

// CloseOrder 关闭订单（仿 Jeepay 关单）
func (p *PaymentProvider) CloseOrder(ctx context.Context, payOrderNo string) error {
	if db.DB() == nil {
		return errDBNotInit
	}
	now := time.Now().UnixMilli()
	affected, err := repository.ClosePayOrder(payOrderNo, now, now)
	if err != nil {
		return err
	}
	if affected == 0 {
		return gorm.ErrRecordNotFound
	}
	_ = repository.CreateOrderRecord(&entity.OrderRecord{
		PayOrderNo: payOrderNo,
		EventType:  "close",
		OldState:   ptrInt16(0),
		NewState:   ptrInt16(2),
		CreatedAt:  now,
	})
	return nil
}

func ptrInt16(v int16) *int16 { return &v }

// StartPayment 启动支付模块 Dubbo 服务
func StartPayment(port int, zkAddr string) error {
	srv, err := NewServer(port, zkAddr)
	if err != nil {
		return err
	}
	if err := Register(srv, &PaymentProvider{}); err != nil {
		return err
	}
	return srv.Serve()
}
