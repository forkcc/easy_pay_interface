package merchant

import (
	"net/http"
	"strconv"

	"github.com/easypay/easy_pay_interface/internal/entity"
	"github.com/easypay/easy_pay_interface/internal/middleware"
	"github.com/easypay/easy_pay_interface/internal/response"
	"github.com/easypay/easy_pay_interface/internal/repository"
	"github.com/gin-gonic/gin"
)
func Routes(g *gin.RouterGroup, merchantRepo *repository.MerchantRepo, payOrderRepo *repository.PayOrderRepo, accountRepo *repository.AccountRepo,
	merchantProductRepo *repository.MerchantProductRepo, paymentProductRepo *repository.PaymentProductRepo, withdrawOrderRepo *repository.WithdrawOrderRepo) {
	g.GET("/health", func(c *gin.Context) { c.JSON(http.StatusOK, response.OK("ok")) })
	g.GET("/balance", balance(accountRepo))
	g.GET("/order/page", orderPage(payOrderRepo))
	g.GET("/product/list", productList(merchantProductRepo, paymentProductRepo))
	g.POST("/withdraw/apply", withdrawApply(accountRepo, withdrawOrderRepo))
	g.GET("/withdraw/page", withdrawPage(withdrawOrderRepo))
}

func balance(repo *repository.AccountRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		merchantID := middleware.GetMerchantID(c)
		a, err := repo.GetByMerchantID(merchantID)
		if err != nil {
			c.JSON(http.StatusOK, response.OK(gin.H{"balance": int64(0), "frozen": int64(0)}))
			return
		}
		c.JSON(http.StatusOK, response.OK(gin.H{
			"balance": a.Balance - a.Frozen,
			"frozen":  a.Frozen,
		}))
	}
}

func orderPage(repo *repository.PayOrderRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		merchantID := middleware.GetMerchantID(c)
		page, _ := strconv.ParseInt(c.DefaultQuery("current", "1"), 10, 64)
		size, _ := strconv.ParseInt(c.DefaultQuery("size", "20"), 10, 64)
		list, total, err := repo.ListByMerchantID(merchantID, page, size)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		type vo struct {
			PayOrderNo string `json:"payOrderNo"`
			Amount     int64  `json:"amount"`
			Status     int8   `json:"status"`
			CreatedAt  string `json:"createdAt"`
		}
		records := make([]vo, 0, len(list))
		for _, o := range list {
			records = append(records, vo{
				PayOrderNo: o.PayOrderNo,
				Amount:     o.Amount,
				Status:     o.Status,
				CreatedAt:  o.CreatedAt.Format("2006-01-02 15:04:05"),
			})
		}
		c.JSON(http.StatusOK, response.OK(response.PageResult[vo]{
			Current: page, Size: size, Total: total, Records: records,
		}))
	}
}

func productList(mpRepo *repository.MerchantProductRepo, productRepo *repository.PaymentProductRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		merchantID := middleware.GetMerchantID(c)
		productIDs, err := mpRepo.ListProductIDsByMerchantID(merchantID)
		if err != nil || len(productIDs) == 0 {
			c.JSON(http.StatusOK, response.OK([]entity.PaymentProduct{}))
			return
		}
		list := make([]entity.PaymentProduct, 0, len(productIDs))
		for _, id := range productIDs {
			p, err := productRepo.GetByID(id)
			if err != nil {
				continue
			}
			list = append(list, *p)
		}
		c.JSON(http.StatusOK, response.OK(list))
	}
}

func withdrawApply(accountRepo *repository.AccountRepo, withdrawRepo *repository.WithdrawOrderRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		merchantID := middleware.GetMerchantID(c)
		var req struct {
			Amount int64 `json:"amount" binding:"required,gt=0"`
		}
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusOK, response.Fail(400, err.Error()))
			return
		}
		acc, err := accountRepo.GetByMerchantID(merchantID)
		if err != nil || acc == nil {
			c.JSON(http.StatusOK, response.Fail(400, "账户不存在"))
			return
		}
		available := acc.Balance - acc.Frozen
		if available < req.Amount {
			c.JSON(http.StatusOK, response.Fail(400, "可用余额不足"))
			return
		}
		o := &entity.WithdrawOrder{MerchantID: merchantID, Amount: req.Amount, Status: entity.WithdrawStatusPending}
		if err := withdrawRepo.Create(o); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK(gin.H{
			"id":        o.ID,
			"amount":    o.Amount,
			"status":    o.Status,
			"createdAt": o.CreatedAt.Format("2006-01-02 15:04:05"),
		}))
	}
}

func withdrawPage(repo *repository.WithdrawOrderRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		merchantID := middleware.GetMerchantID(c)
		page, _ := strconv.ParseInt(c.DefaultQuery("current", "1"), 10, 64)
		size, _ := strconv.ParseInt(c.DefaultQuery("size", "20"), 10, 64)
		list, total, err := repo.ListByMerchantID(merchantID, page, size)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK(response.PageResult[entity.WithdrawOrder]{
			Current: page, Size: size, Total: total, Records: list,
		}))
	}
}
