package config

import (
	"os"
	"strconv"
)

// Config 从环境变量读取配置
type Config struct {
	DBDSN            string // PostgreSQL 连接串，环境变量 DB_DSN
	TelegramBotToken string
	RabbitMQURL      string
	RedisAddr        string
	RedisPassword    string
	RedisDB          int    // 默认 0
	ZookeeperAddr    string // Dubbo 注册中心，如 127.0.0.1:2181
}

// Load 加载配置
func Load() *Config {
	c := &Config{}
	c.DBDSN = os.Getenv("DB_DSN")
	if c.DBDSN == "" {
		c.DBDSN = "host=localhost user=postgres password=postgres dbname=easy_pay port=5432 sslmode=disable TimeZone=Asia/Shanghai"
	}
	c.TelegramBotToken = os.Getenv("TELEGRAM_BOT_TOKEN")
	c.RabbitMQURL = os.Getenv("RABBITMQ_URL")
	if c.RabbitMQURL == "" {
		c.RabbitMQURL = "amqp://guest:guest@localhost:5672/"
	}
	c.RedisAddr = os.Getenv("REDIS_ADDR")
	if c.RedisAddr == "" {
		c.RedisAddr = "localhost:6379"
	}
	c.RedisPassword = os.Getenv("REDIS_PASSWORD")
	if d := os.Getenv("REDIS_DB"); d != "" {
		if v, err := strconv.Atoi(d); err == nil && v >= 0 {
			c.RedisDB = v
		}
	}
	c.ZookeeperAddr = os.Getenv("ZK_ADDR")
	if c.ZookeeperAddr == "" {
		c.ZookeeperAddr = "127.0.0.1:2181"
	}
	return c
}
