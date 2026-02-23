package main

import (
	"context"
	"log"
	"os"
	"os/signal"
	"syscall"

	"github.com/easypay/easy_pay_interface/internal/app"
	"github.com/easypay/easy_pay_interface/internal/bot"
)

func main() {
	cfg := bot.LoadConfig()
	if cfg.Token == "" {
		log.Println("未设置 BOT_TOKEN，仅启动 HTTP 推送服务（无机器人）")
	}

	db, err := app.OpenDB()
	if err != nil {
		log.Fatal("db: ", err)
	}

	svr, err := bot.NewServer(cfg, db)
	if err != nil {
		log.Fatal("bot: ", err)
	}

	ctx, stop := signal.NotifyContext(context.Background(), os.Interrupt, syscall.SIGTERM)
	defer stop()

	if err := svr.Run(ctx); err != nil {
		log.Fatal(err)
	}
}
