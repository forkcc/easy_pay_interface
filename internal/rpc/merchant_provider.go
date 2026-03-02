package rpc

// MerchantProvider 商户模块 Dubbo 接口（参考 Jeepay 商户系统）
type MerchantProvider struct{}

func (p *MerchantProvider) Reference() string { return "MerchantProvider" }

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
