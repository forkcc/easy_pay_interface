package redis

import (
	"context"
	"log"
	"time"

	"github.com/easypay/easy_pay_interface/internal/config"
	"github.com/redis/go-redis/v9"
)

// Client 封装 Redis 客户端
type Client struct {
	*redis.Client
}

// Open 创建 Redis 客户端（addr 为空时返回 nil）
func Open(addr, password string, db int) *Client {
	if addr == "" {
		addr = "localhost:6379"
	}
	cli := redis.NewClient(&redis.Options{
		Addr:         addr,
		Password:     password,
		DB:           db,
		DialTimeout:  5 * time.Second,
		ReadTimeout:  3 * time.Second,
		WriteTimeout: 3 * time.Second,
	})
	return &Client{Client: cli}
}

// OpenFromEnv 从 config 读取 Redis 配置并创建客户端
func OpenFromEnv() *Client {
	cfg := config.Load()
	cli := Open(cfg.RedisAddr, cfg.RedisPassword, cfg.RedisDB)
	if err := cli.Ping(context.Background()); err != nil {
		log.Printf("redis: ping failed (%v), client still returned", err)
		return cli
	}
	log.Println("redis: connected")
	return cli
}

// Ping 检查连接
func (c *Client) Ping(ctx context.Context) error {
	if c == nil || c.Client == nil {
		return nil
	}
	return c.Client.Ping(ctx).Err()
}

// Close 关闭连接
func (c *Client) Close() error {
	if c == nil || c.Client == nil {
		return nil
	}
	return c.Client.Close()
}

