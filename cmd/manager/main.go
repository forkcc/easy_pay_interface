// 管理端模块：仅暴露 Dubbo 接口，独立进程。参考 Jeepay / 龙果 管理后台。
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
		log.Fatal("manager: db init: ", err)
	}
	port := 20004
	if p := os.Getenv("DUBBO_PORT"); p != "" {
		if v, err := strconv.Atoi(p); err == nil && v > 0 {
			port = v
		}
	}
	log.Printf("manager: Dubbo port %d, zk=%s", port, cfg.ZookeeperAddr)
	if err := rpc.StartManager(port, cfg.ZookeeperAddr); err != nil {
		log.Fatal("manager: ", err)
	}
}
