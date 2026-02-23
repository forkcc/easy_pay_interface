package service

import (
	"bytes"
	"encoding/json"
	"net/http"
	"time"

	"github.com/easypay/easy_pay_interface/internal/entity"
	"github.com/easypay/easy_pay_interface/internal/repository"
)

type NotifyService struct {
	notifyLogRepo *repository.NotifyLogRepo
	payOrderRepo  *repository.PayOrderRepo
}

func NewNotifyService(notifyLogRepo *repository.NotifyLogRepo, payOrderRepo *repository.PayOrderRepo) *NotifyService {
	return &NotifyService{notifyLogRepo: notifyLogRepo, payOrderRepo: payOrderRepo}
}

// SendAsync 创建通知记录后异步 POST 到商户 notify_url；若 HTTP 200 则更新 pay_order.notify_status=1
func (s *NotifyService) SendAsync(log *entity.NotifyLog, payOrderID uint) {
	go func() {
		req, _ := http.NewRequest(http.MethodPost, log.NotifyURL, bytes.NewBufferString(log.ReqBody))
		req.Header.Set("Content-Type", "application/json")
		client := &http.Client{Timeout: 10 * time.Second}
		resp, err := client.Do(req)
		status := 0
		var body string
		if err != nil {
			body = err.Error()
		} else {
			status = resp.StatusCode
			defer resp.Body.Close()
			buf := make([]byte, 4096)
			n, _ := resp.Body.Read(buf)
			body = string(buf[:n])
		}
		log.RespStatus = &status
		log.RespBody = body
		_ = s.notifyLogRepo.Update(log)
		if status == 200 && payOrderID > 0 {
			o, _ := s.payOrderRepo.GetByID(payOrderID)
			if o != nil {
				o.NotifyStatus = 1
				_ = s.payOrderRepo.Update(o)
			}
		}
	}()
}

// GetByID 用于重试时获取 log
func (s *NotifyService) GetByID(id uint) (*entity.NotifyLog, error) {
	return s.notifyLogRepo.GetByID(id)
}

func (s *NotifyService) Update(log *entity.NotifyLog) error {
	return s.notifyLogRepo.Update(log)
}

const maxRetryCount = 5

// RetryPending 扫描未成功的通知记录并重试 POST
func (s *NotifyService) RetryPending(limit int) {
	list, err := s.notifyLogRepo.ListPending(maxRetryCount, limit)
	if err != nil || len(list) == 0 {
		return
	}
	for i := range list {
		go s.doSend(&list[i])
	}
}

func (s *NotifyService) doSend(log *entity.NotifyLog) {
	req, _ := http.NewRequest(http.MethodPost, log.NotifyURL, bytes.NewBufferString(log.ReqBody))
	req.Header.Set("Content-Type", "application/json")
	client := &http.Client{Timeout: 10 * time.Second}
	resp, err := client.Do(req)
	status := 0
	var body string
	if err != nil {
		body = err.Error()
	} else {
		status = resp.StatusCode
		defer resp.Body.Close()
		buf := make([]byte, 4096)
		n, _ := resp.Body.Read(buf)
		body = string(buf[:n])
	}
	log.RespStatus = &status
	log.RespBody = body
	log.RetryCount++
	_ = s.notifyLogRepo.Update(log)
}

// BuildPayBody 构建支付成功通知 body
func BuildPayBody(payOrderNo string, amount int64, status int8, upstreamSn string) string {
	b, _ := json.Marshal(map[string]interface{}{
		"payOrderNo": payOrderNo,
		"amount":    amount,
		"status":    status,
		"upstreamSn": upstreamSn,
	})
	return string(b)
}

// BuildRefundBody 构建退款成功通知 body
func BuildRefundBody(refundOrderNo string, amount int64, status int8) string {
	b, _ := json.Marshal(map[string]interface{}{
		"refundOrderNo": refundOrderNo,
		"amount":        amount,
		"status":        status,
	})
	return string(b)
}
