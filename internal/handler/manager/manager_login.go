package manager

import (
	"net/http"

	"github.com/easypay/easy_pay_interface/internal/entity"
	"github.com/easypay/easy_pay_interface/internal/response"
	"github.com/easypay/easy_pay_interface/internal/repository"
	"github.com/easypay/easy_pay_interface/internal/service"
	"github.com/gin-gonic/gin"
)

func managerLogin(adminUserRepo *repository.AdminUserRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		var req struct {
			Username string `json:"username" binding:"required"`
			Password string `json:"password" binding:"required"`
		}
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusOK, response.Fail(400, "参数错误"))
			return
		}
		u, err := adminUserRepo.GetByUsername(req.Username)
		if err != nil || u == nil {
			c.JSON(http.StatusOK, response.Fail(401, "用户名或密码错误"))
			return
		}
		if !service.CheckPassword(u.PasswordHash, req.Password) {
			c.JSON(http.StatusOK, response.Fail(401, "用户名或密码错误"))
			return
		}
		token, exp, err := service.IssueAdminToken(u.ID, u.Username)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(500, "生成令牌失败"))
			return
		}
		c.JSON(http.StatusOK, response.OK(gin.H{
			"token":    token,
			"expireAt": exp.Format("2006-01-02T15:04:05Z07:00"),
			"user": gin.H{
				"id":       u.ID,
				"username": u.Username,
				"name":     u.Name,
			},
		}))
	}
}

func managerInit(adminUserRepo *repository.AdminUserRepo) gin.HandlerFunc {
	return func(c *gin.Context) {
		n, err := adminUserRepo.Count()
		if err != nil || n > 0 {
			c.JSON(http.StatusOK, response.Fail(400, "已有管理员账号，无法初始化"))
			return
		}
		var req struct {
			Username string `json:"username" binding:"required"`
			Password string `json:"password" binding:"required"`
			Name     string `json:"name"`
		}
		if err := c.ShouldBindJSON(&req); err != nil {
			c.JSON(http.StatusOK, response.Fail(400, "参数错误"))
			return
		}
		hash, err := service.HashPassword(req.Password)
		if err != nil {
			c.JSON(http.StatusOK, response.Fail(500, "加密失败"))
			return
		}
		u := &entity.AdminUser{Username: req.Username, PasswordHash: hash, Name: req.Name, Status: 1}
		if err := adminUserRepo.Create(u); err != nil {
			c.JSON(http.StatusOK, response.Fail(500, err.Error()))
			return
		}
		c.JSON(http.StatusOK, response.OK(gin.H{"id": u.ID, "username": u.Username}))
	}
}
