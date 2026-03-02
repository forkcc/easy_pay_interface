package rpc

// PaymentProvider 支付模块 Dubbo 接口（参考 Jeepay 支付网关 / Roncoo 支付网关）
type PaymentProvider struct{}

func (p *PaymentProvider) Reference() string { return "PaymentProvider" }

// StartPayment 启动支付模块 Dubbo 服务
func StartPayment(port int, zkAddr string) error {
	srv, err := NewServer(port, zkAddr)
	if err != nil {
		return err
	}
	if err := Register(srv, &PaymentProvider{}); err != nil {
		return err
	}
	return srv.Serve()
}
