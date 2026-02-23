package middleware

import (
	"net/http"
	"sync/atomic"

	"github.com/gin-gonic/gin"
)

var (
	metricsTotalRequests atomic.Uint64
	metricsTotalErrors   atomic.Uint64
)

// MetricsCollector 统计请求数与错误数（status >= 400 计为错误）
func MetricsCollector() gin.HandlerFunc {
	return func(c *gin.Context) {
		metricsTotalRequests.Add(1)
		c.Next()
		if c.Writer.Status() >= 400 {
			metricsTotalErrors.Add(1)
		}
	}
}

// MetricsSnapshot 返回当前统计快照，供 GET /metrics 使用
func MetricsSnapshot() gin.H {
	return gin.H{
		"total_requests": metricsTotalRequests.Load(),
		"total_errors":   metricsTotalErrors.Load(),
	}
}

// MetricsHandler 返回 JSON 格式的指标，可用于监控与告警
func MetricsHandler(c *gin.Context) {
	c.JSON(http.StatusOK, MetricsSnapshot())
}
