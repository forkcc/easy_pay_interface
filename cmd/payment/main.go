// 支付模块：仅暴露 Dubbo 接口，独立进程。参考 Jeepay 支付网关 / Roncoo 支付网关。
package main

import (
	"log"
	"os"
	"strconv"

	_ "dubbo.apache.org/dubbo-go/v3/imports"
	"github.com/easypay/easy_pay_interface/internal/config"
	"github.com/easypay/easy_pay_interface/internal/rpc"
)

func main() {
	cfg := config.Load()
	port := 20003
	if p := os.Getenv("DUBBO_PORT"); p != "" {
		if v, err := strconv.Atoi(p); err == nil && v > 0 {
			port = v
		}
	}
	log.Printf("payment: Dubbo port %d, zk=%s", port, cfg.ZookeeperAddr)
	if err := rpc.StartPayment(port, cfg.ZookeeperAddr); err != nil {
		log.Fatal("payment: ", err)
	}
}
