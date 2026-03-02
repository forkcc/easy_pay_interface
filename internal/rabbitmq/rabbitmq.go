package rabbitmq

import (
	"log"

	"github.com/easypay/easy_pay_interface/internal/config"
	amqp "github.com/rabbitmq/amqp091-go"
)

// Conn 封装 RabbitMQ 连接与 channel
type Conn struct {
	conn    *amqp.Connection
	channel *amqp.Channel
}

// Open 打开连接（URL 为空时跳过，返回 nil）
func Open(url string) (*Conn, error) {
	if url == "" {
		return nil, nil
	}
	conn, err := amqp.Dial(url)
	if err != nil {
		return nil, err
	}
	ch, err := conn.Channel()
	if err != nil {
		_ = conn.Close()
		return nil, err
	}
	log.Println("rabbitmq: connected")
	return &Conn{conn: conn, channel: ch}, nil
}

// OpenFromEnv 从 config 读取 RABBITMQ_URL 并打开连接
func OpenFromEnv() (*Conn, error) {
	url := config.Load().RabbitMQURL
	return Open(url)
}

// Close 关闭连接
func (c *Conn) Close() error {
	if c == nil {
		return nil
	}
	if c.channel != nil {
		_ = c.channel.Close()
	}
	if c.conn != nil {
		return c.conn.Close()
	}
	return nil
}

// Channel 返回 channel，用于声明队列、发布、消费
func (c *Conn) Channel() *amqp.Channel { return c.channel }

// Publish 向指定 exchange、routingKey 发布消息
func (c *Conn) Publish(exchange, routingKey string, body []byte) error {
	if c == nil || c.channel == nil {
		return nil
	}
	return c.channel.Publish(exchange, routingKey, false, false, amqp.Publishing{
		ContentType: "application/octet-stream",
		Body:        body,
	})
}

// DeclareQueue 声明队列（幂等）
func (c *Conn) DeclareQueue(name string) (amqp.Queue, error) {
	if c == nil || c.channel == nil {
		return amqp.Queue{}, nil
	}
	return c.channel.QueueDeclare(name, true, false, false, false, nil)
}
