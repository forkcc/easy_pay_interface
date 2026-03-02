package rpc

import (
	"context"

	"github.com/easypay/easy_pay_interface/internal/entity"
	"github.com/easypay/easy_pay_interface/internal/repository"
)

// AgentProvider 代理模块 Dubbo 接口（参考 Jeepay 服务商/代理、龙果代理）
type AgentProvider struct{}

func (p *AgentProvider) Reference() string { return "AgentProvider" }

// GetByAgentNo 根据代理号查询代理
func (p *AgentProvider) GetByAgentNo(ctx context.Context, agentNo string) (*entity.Agent, error) {
	return repository.GetAgentByAgentNo(agentNo)
}

// GetByLoginName 根据登录名查询代理（用于登录校验）
func (p *AgentProvider) GetByLoginName(ctx context.Context, loginName string) (*entity.Agent, error) {
	return repository.GetAgentByLoginName(loginName)
}

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
