package rpc

import (
	"context"

	"github.com/easypay/easy_pay_interface/internal/entity"
	"github.com/easypay/easy_pay_interface/internal/repository"
)

// ManagerProvider 管理端模块 Dubbo 接口（参考 Jeepay / 龙果 管理后台账号）
type ManagerProvider struct{}

func (p *ManagerProvider) Reference() string { return "ManagerProvider" }

// GetByLoginName 根据登录名查询管理端用户（用于登录校验）
func (p *ManagerProvider) GetByLoginName(ctx context.Context, loginName string) (*entity.ManagerUser, error) {
	return repository.GetManagerByLoginName(loginName)
}

// GetByID 根据主键查询管理端用户
func (p *ManagerProvider) GetByID(ctx context.Context, id int64) (*entity.ManagerUser, error) {
	return repository.GetManagerByID(id)
}

// StartManager 启动管理端模块 Dubbo 服务
func StartManager(port int, zkAddr string) error {
	srv, err := NewServer(port, zkAddr)
	if err != nil {
		return err
	}
	if err := Register(srv, &ManagerProvider{}); err != nil {
		return err
	}
	return srv.Serve()
}
