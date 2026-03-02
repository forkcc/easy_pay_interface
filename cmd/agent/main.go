// 代理模块：仅暴露 Dubbo 接口，独立进程。参考 Jeepay 服务商/代理模式。
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
	port := 20002
	if p := os.Getenv("DUBBO_PORT"); p != "" {
		if v, err := strconv.Atoi(p); err == nil && v > 0 {
			port = v
		}
	}
	log.Printf("agent: Dubbo port %d, zk=%s", port, cfg.ZookeeperAddr)
	if err := rpc.StartAgent(port, cfg.ZookeeperAddr); err != nil {
		log.Fatal("agent: ", err)
	}
}
