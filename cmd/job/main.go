package main

import (
	"log"
	"time"

	"github.com/easypay/easy_pay_interface/internal/app"
	"github.com/easypay/easy_pay_interface/internal/repository"
	"github.com/easypay/easy_pay_interface/internal/service"
)

func main() {
	db, err := app.OpenDB()
	if err != nil {
		log.Fatal("db: ", err)
	}

	notifyLogRepo := repository.NewNotifyLogRepo(db)
	payOrderRepo := repository.NewPayOrderRepo(db)
	notifySvc := service.NewNotifyService(notifyLogRepo, payOrderRepo)

	reconcileReportRepo := repository.NewReconcileReportRepo(db)
	refundOrderRepo := repository.NewRefundOrderRepo(db)

	ticker := time.NewTicker(5 * time.Minute)
	defer ticker.Stop()
	log.Println("job started (notify retry + reconcile)")
	for range ticker.C {
		notifySvc.RetryPending(50)
		log.Println("notify retry tick done")

		yesterday := time.Now().AddDate(0, 0, -1)
		if err := service.ReconcileRun(payOrderRepo, refundOrderRepo, reconcileReportRepo, yesterday); err != nil {
			log.Println("reconcile error:", err)
		} else {
			log.Println("reconcile done for", yesterday.Format("2006-01-02"))
		}
	}
}
