package rpc

import (
	"context"

	"github.com/easypay/easy_pay_interface/internal/entity"
	"github.com/easypay/easy_pay_interface/internal/repository"
)

// MerchantProvider 商户模块 Dubbo 接口（参考 Jeepay 商户系统 / 龙果商户）
type MerchantProvider struct{}

func (p *MerchantProvider) Reference() string { return "MerchantProvider" }

// GetByMchNo 根据商户号查询商户（仿 Jeepay 商户信息查询）
func (p *MerchantProvider) GetByMchNo(ctx context.Context, mchNo string) (*entity.Merchant, error) {
	return repository.GetMerchantByMchNo(mchNo)
}

// GetByLoginName 根据登录名查询商户（用于登录校验）
func (p *MerchantProvider) GetByLoginName(ctx context.Context, loginName string) (*entity.Merchant, error) {
	return repository.GetMerchantByLoginName(loginName)
}

// StartMerchant 启动商户模块 Dubbo 服务
func StartMerchant(port int, zkAddr string) error {
	srv, err := NewServer(port, zkAddr)
	if err != nil {
		return err
	}
	if err := Register(srv, &MerchantProvider{}); err != nil {
		return err
	}
	return srv.Serve()
}
