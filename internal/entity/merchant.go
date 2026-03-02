package entity

// Merchant 商户主表
type Merchant struct {
	ID            int64  `gorm:"primaryKey;autoIncrement"`
	MchNo         string `gorm:"column:mch_no;type:varchar(32);uniqueIndex;not null"`
	MchName       string `gorm:"column:mch_name;type:varchar(64);not null"`
	LoginName     string `gorm:"column:login_name;type:varchar(64);uniqueIndex;not null"`
	PasswordHash  string `gorm:"column:password_hash;type:varchar(128);not null"`
	OtpSecret     *string `gorm:"column:otp_secret;type:varchar(64)"`
	OtpEnabled    int16  `gorm:"column:otp_enabled;not null;default:0"`
	ContactName   *string `gorm:"column:contact_name;type:varchar(32)"`
	ContactTel    *string `gorm:"column:contact_tel;type:varchar(32)"`
	ContactEmail  *string `gorm:"column:contact_email;type:varchar(64)"`
	State         int16  `gorm:"column:state;not null;default:1"`
	Balance       int64  `gorm:"column:balance;not null;default:0"`
	FrozenBalance int64  `gorm:"column:frozen_balance;not null;default:0"`
	LastLoginAt   *int64 `gorm:"column:last_login_at"` // Unix 毫秒
	Remark        *string `gorm:"column:remark;type:varchar(256)"`
	CreatedAt     int64  `gorm:"column:created_at;not null"` // Unix 毫秒
	UpdatedAt     int64  `gorm:"column:updated_at;not null"` // Unix 毫秒
}

func (Merchant) TableName() string { return "merchant.merchant" }

// MerchantFundFlow 商户资金流水
type MerchantFundFlow struct {
	ID            int64     `gorm:"primaryKey;autoIncrement"`
	MchNo         string    `gorm:"column:mch_no;type:varchar(32);index;not null"`
	FlowType      int16     `gorm:"column:flow_type;not null"`
	Amount        int64     `gorm:"column:amount;not null"`
	BeforeBalance int64     `gorm:"column:before_balance;not null;default:0"`
	AfterBalance  int64     `gorm:"column:after_balance;not null;default:0"`
	RefType       *string `gorm:"column:ref_type;type:varchar(32)"`
	RefNo         *string `gorm:"column:ref_no;type:varchar(64)"`
	Remark        *string `gorm:"column:remark;type:varchar(256)"`
	CreatedAt     int64   `gorm:"column:created_at;not null"` // Unix 毫秒
}

func (MerchantFundFlow) TableName() string { return "merchant.merchant_fund_flow" }

// MerchantCallbackLog 商户异步通知回调记录
type MerchantCallbackLog struct {
	ID             int64      `gorm:"primaryKey;autoIncrement"`
	MchNo          string     `gorm:"column:mch_no;type:varchar(32);index;not null"`
	PayOrderNo     string     `gorm:"column:pay_order_no;type:varchar(64);index;not null"`
	CallbackURL    string     `gorm:"column:callback_url;type:varchar(512);not null"`
	CallbackCount  int32      `gorm:"column:callback_count;not null;default:0"`
	ResponseCode   *int       `gorm:"column:response_code"`
	ResponseBody   *string    `gorm:"column:response_body;type:text"`
	State          int16  `gorm:"column:state;not null;default:0"`
	NextCallbackAt *int64 `gorm:"column:next_callback_at"` // Unix 毫秒
	CreatedAt      int64  `gorm:"column:created_at;not null"` // Unix 毫秒
	UpdatedAt      int64  `gorm:"column:updated_at;not null"` // Unix 毫秒
}

func (MerchantCallbackLog) TableName() string { return "merchant.merchant_callback_log" }
