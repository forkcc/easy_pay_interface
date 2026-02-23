package service

import (
	"time"

	"github.com/easypay/easy_pay_interface/internal/entity"
	"github.com/easypay/easy_pay_interface/internal/repository"
)

// ReconcileRun 对指定日期跑对账：按「创建时间」「更新时间」各跑一份，按商户汇总当日支付成功、退款成功笔数与金额，写入 reconcile_report
func ReconcileRun(
	payOrderRepo *repository.PayOrderRepo,
	refundOrderRepo *repository.RefundOrderRepo,
	reconcileReportRepo *repository.ReconcileReportRepo,
	reconcileDate time.Time,
) error {
	start := time.Date(reconcileDate.Year(), reconcileDate.Month(), reconcileDate.Day(), 0, 0, 0, 0, reconcileDate.Location())
	end := start.Add(24 * time.Hour)

	for _, timeType := range []string{entity.ReconcileTimeTypeCreated, entity.ReconcileTimeTypeUpdated} {
		byCreated := timeType == entity.ReconcileTimeTypeCreated
		var paySums, refundSums []struct {
			MerchantID uint
			Count      int64
			Amount     int64
		}
		var err error
		if byCreated {
			paySums, err = payOrderRepo.SumSuccessByDate(start, end)
		} else {
			paySums, err = payOrderRepo.SumSuccessByDateByUpdated(start, end)
		}
		if err != nil {
			return err
		}
		if byCreated {
			refundSums, err = refundOrderRepo.SumSuccessByDate(start, end)
		} else {
			refundSums, err = refundOrderRepo.SumSuccessByDateByUpdated(start, end)
		}
		if err != nil {
			return err
		}

		payMap := make(map[uint]struct{ Count int64; Amount int64 })
		for _, s := range paySums {
			payMap[s.MerchantID] = struct{ Count int64; Amount int64 }{s.Count, s.Amount}
		}
		refundMap := make(map[uint]struct{ Count int64; Amount int64 })
		for _, s := range refundSums {
			refundMap[s.MerchantID] = struct{ Count int64; Amount int64 }{s.Count, s.Amount}
		}

		merchantIDs := make(map[uint]bool)
		for id := range payMap {
			merchantIDs[id] = true
		}
		for id := range refundMap {
			merchantIDs[id] = true
		}

		for mid := range merchantIDs {
			p := payMap[mid]
			r := refundMap[mid]
			row, err := reconcileReportRepo.GetByDateAndMerchantAndTimeType(start, mid, timeType)
			if err != nil {
				row = &entity.ReconcileReport{
					ReconcileDate: start,
					MerchantID:    mid,
					TimeType:      timeType,
				}
			}
			row.PayCount = int(p.Count)
			row.PayAmount = p.Amount
			row.RefundCount = int(r.Count)
			row.RefundAmount = r.Amount
			if err := reconcileReportRepo.Save(row); err != nil {
				return err
			}
		}
	}
	return nil
}
