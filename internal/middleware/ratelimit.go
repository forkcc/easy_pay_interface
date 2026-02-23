package middleware

import (
	"net/http"
	"sync"
	"time"

	"github.com/gin-gonic/gin"
)

// bucket 固定窗口计数
type rateBucket struct {
	count     int
	windowEnd time.Time
}

// RateLimitGin 固定窗口限流：每 window 内每 key 最多 limit 次请求
// keyFunc: 从 gin.Context 提取 key，如按 IP 或按 path
func RateLimitGin(limit int, window time.Duration, keyFunc func(*gin.Context) string) gin.HandlerFunc {
	var mu sync.Mutex
	m := make(map[string]*rateBucket)
	return func(c *gin.Context) {
		key := keyFunc(c)
		if key == "" {
			key = "default"
		}
		mu.Lock()
		b, ok := m[key]
		now := time.Now()
		if !ok || now.After(b.windowEnd) {
			b = &rateBucket{count: 0, windowEnd: now.Add(window)}
			m[key] = b
		}
		b.count++
		allowed := b.count <= limit
		mu.Unlock()
		if !allowed {
			c.AbortWithStatusJSON(http.StatusTooManyRequests, gin.H{"code": 429, "msg": "too many requests"})
			return
		}
		c.Next()
	}
}

// KeyByClientIP 从 X-Forwarded-For 或 ClientIP 作为限流 key
func KeyByClientIP(c *gin.Context) string {
	return c.ClientIP()
}

// KeyByPath 按请求路径限流（同一 path 共享计数）
func KeyByPath(c *gin.Context) string {
	return c.FullPath()
}
