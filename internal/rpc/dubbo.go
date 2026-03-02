package rpc

import (
	"dubbo.apache.org/dubbo-go/v3/protocol"
	"dubbo.apache.org/dubbo-go/v3/registry"
	"dubbo.apache.org/dubbo-go/v3/server"
)

const registryID = "zk"

// NewServer 创建 Dubbo 服务端，可选注册到 Zookeeper
func NewServer(port int, zkAddr string, opts ...server.ServerOption) (*server.Server, error) {
	base := []server.ServerOption{
		server.WithServerProtocol(
			protocol.WithDubbo(),
			protocol.WithPort(port),
		),
	}
	if zkAddr != "" {
		base = append(base,
			server.WithServerRegistry(
				registry.WithZookeeper(),
				registry.WithID(registryID),
				registry.WithAddress(zkAddr),
			),
			server.WithServerRegistryIDs([]string{registryID}),
		)
	}
	return server.NewServer(append(base, opts...)...)
}

// Register 将 Provider 注册到 Server（Provider 需实现 Reference() string）
func Register(srv *server.Server, provider interface{}) error {
	if ref, ok := provider.(interface{ Reference() string }); ok {
		return srv.Register(provider, nil, server.WithInterface(ref.Reference()))
	}
	return nil
}
