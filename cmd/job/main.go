// job 模块：定时任务（通知重试、对账等），独立运行。
package main

import (
	"log"
	"time"

	"github.com/easypay/easy_pay_interface/internal/config"
)

func main() {
	config.Load()
	log.Println("job: started (scaffold)")
	ticker := time.NewTicker(5 * time.Minute)
	defer ticker.Stop()
	for range ticker.C {
		// TODO: 通知重试、对账
	}
}
