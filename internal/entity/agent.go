package entity

// Agent 代理主表（含登录、OTP、余额）
type Agent struct {
	ID            int64  `gorm:"primaryKey;autoIncrement"`
	AgentNo       string `gorm:"column:agent_no;type:varchar(32);uniqueIndex;not null"`
	AgentName     string `gorm:"column:agent_name;type:varchar(64);not null"`
	LoginName     string `gorm:"column:login_name;type:varchar(64);uniqueIndex;not null"`
	PasswordHash  string `gorm:"column:password_hash;type:varchar(128);not null"`
	OtpSecret     *string `gorm:"column:otp_secret;type:varchar(64)"`
	OtpEnabled    int16  `gorm:"column:otp_enabled;not null;default:0"`
	ContactName   *string `gorm:"column:contact_name;type:varchar(32)"`
	ContactTel    *string `gorm:"column:contact_tel;type:varchar(32)"`
	State         int16  `gorm:"column:state;not null;default:1"`
	Balance       int64  `gorm:"column:balance;not null;default:0"`
	FrozenBalance int64  `gorm:"column:frozen_balance;not null;default:0"`
	LastLoginAt   *int64 `gorm:"column:last_login_at"` // Unix 毫秒
	Remark        *string `gorm:"column:remark;type:varchar(256)"`
	CreatedAt     int64  `gorm:"column:created_at;not null"` // Unix 毫秒
	UpdatedAt     int64  `gorm:"column:updated_at;not null"` // Unix 毫秒
}

func (Agent) TableName() string { return "agent.agent" }

// AgentFundFlow 代理资金流水
type AgentFundFlow struct {
	ID            int64     `gorm:"primaryKey;autoIncrement"`
	AgentNo       string    `gorm:"column:agent_no;type:varchar(32);index;not null"`
	FlowType      int16     `gorm:"column:flow_type;not null"`
	Amount        int64     `gorm:"column:amount;not null"`
	BeforeBalance int64     `gorm:"column:before_balance;not null;default:0"`
	AfterBalance  int64     `gorm:"column:after_balance;not null;default:0"`
	RefType   *string `gorm:"column:ref_type;type:varchar(32)"`
	RefNo     *string `gorm:"column:ref_no;type:varchar(64)"`
	Remark    *string `gorm:"column:remark;type:varchar(256)"`
	CreatedAt int64   `gorm:"column:created_at;not null"` // Unix 毫秒
}

func (AgentFundFlow) TableName() string { return "agent.agent_fund_flow" }

// AgentMerchant 代理与商户绑定关系
type AgentMerchant struct {
	ID        int64          `gorm:"primaryKey;autoIncrement"`
	AgentNo   string         `gorm:"column:agent_no;type:varchar(32);uniqueIndex:uidx_agent_merchant;index;not null"`
	MchNo     string         `gorm:"column:mch_no;type:varchar(32);uniqueIndex:uidx_agent_merchant;index;not null"`
	BindTime  int64 `gorm:"column:bind_time;not null"` // Unix 毫秒
	State     int16 `gorm:"column:state;not null;default:1"`
	CreatedAt int64 `gorm:"column:created_at;not null"` // Unix 毫秒
	UpdatedAt int64 `gorm:"column:updated_at;not null"` // Unix 毫秒
}

func (AgentMerchant) TableName() string { return "agent.agent_merchant" }
