// 商户模块：仅暴露 Dubbo 接口，独立进程。参考 Jeepay 商户系统。
package main

import (
	"log"
	"os"
	"strconv"

	_ "dubbo.apache.org/dubbo-go/v3/imports"
	"github.com/easypay/easy_pay_interface/internal/config"
	"github.com/easypay/easy_pay_interface/internal/db"
	"github.com/easypay/easy_pay_interface/internal/rpc"
)

func main() {
	cfg := config.Load()
	if err := db.Init(cfg.DBDSN); err != nil {
		log.Fatal("merchant: db init: ", err)
	}
	port := 20001
	if p := os.Getenv("DUBBO_PORT"); p != "" {
		if v, err := strconv.Atoi(p); err == nil && v > 0 {
			port = v
		}
	}
	log.Printf("merchant: Dubbo port %d, zk=%s", port, cfg.ZookeeperAddr)
	if err := rpc.StartMerchant(port, cfg.ZookeeperAddr); err != nil {
		log.Fatal("merchant: ", err)
	}
}
