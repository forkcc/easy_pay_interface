package bot

import (
	"log"
	"os"
	"strings"
)

type Config struct {
	Token         string   // BOT_TOKEN
	SuperAccounts []string // BOT_SUPER_ACCOUNTS 逗号分隔的 Telegram 用户名（不含 @）
}

func LoadConfig() Config {
	token := os.Getenv("BOT_TOKEN")
	if token == "" {
		log.Println("BOT_TOKEN not set, bot will not start")
	}
	var superAccounts []string
	if s := os.Getenv("BOT_SUPER_ACCOUNTS"); s != "" {
		for _, v := range strings.Split(s, ",") {
			v = strings.TrimSpace(v)
			if v == "" {
				continue
			}
			// 去掉可能带的前缀 @
			v = strings.TrimPrefix(v, "@")
			superAccounts = append(superAccounts, strings.ToLower(v))
		}
	}
	return Config{Token: token, SuperAccounts: superAccounts}
}

// IsSuper 根据 Telegram 用户名判断是否为超级用户（大小写不敏感）
func (c *Config) IsSuper(username string) bool {
	username = strings.ToLower(strings.TrimPrefix(username, "@"))
	for _, a := range c.SuperAccounts {
		if a == username {
			return true
		}
	}
	return false
}
