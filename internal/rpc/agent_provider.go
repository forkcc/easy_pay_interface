package rpc

// AgentProvider 代理模块 Dubbo 接口（参考 Jeepay 服务商/代理模式）
type AgentProvider struct{}

func (p *AgentProvider) Reference() string { return "AgentProvider" }

// StartAgent 启动代理模块 Dubbo 服务
func StartAgent(port int, zkAddr string) error {
	srv, err := NewServer(port, zkAddr)
	if err != nil {
		return err
	}
	if err := Register(srv, &AgentProvider{}); err != nil {
		return err
	}
	return srv.Serve()
}
