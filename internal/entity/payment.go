package entity

// PayType 支付类型
type PayType struct {
	ID        int64  `gorm:"primaryKey;autoIncrement"`
	TypeCode  string `gorm:"column:type_code;type:varchar(32);uniqueIndex;not null"`
	TypeName  string `gorm:"column:type_name;type:varchar(64);not null"`
	SortNo    int32  `gorm:"column:sort_no;not null;default:0"`
	State     int16  `gorm:"column:state;not null;default:1"`
	CreatedAt int64  `gorm:"column:created_at;not null"` // Unix 毫秒
	UpdatedAt int64  `gorm:"column:updated_at;not null"` // Unix 毫秒
}

func (PayType) TableName() string { return "payment.pay_type" }

// PayInterface 支付接口
type PayInterface struct {
	ID        int64   `gorm:"primaryKey;autoIncrement"`
	IfCode    string  `gorm:"column:if_code;type:varchar(32);uniqueIndex;not null"`
	IfName    string  `gorm:"column:if_name;type:varchar(64);not null"`
	TypeCode  string  `gorm:"column:type_code;type:varchar(32);index;not null"`
	State     int16   `gorm:"column:state;not null;default:1"`
	Remark    *string `gorm:"column:remark;type:varchar(256)"`
	CreatedAt int64   `gorm:"column:created_at;not null"` // Unix 毫秒
	UpdatedAt int64   `gorm:"column:updated_at;not null"` // Unix 毫秒
}

func (PayInterface) TableName() string { return "payment.pay_interface" }

// PayChannel 支付渠道（商户+应用+方式）
type PayChannel struct {
	ID             int64     `gorm:"primaryKey;autoIncrement"`
	MchNo          string    `gorm:"column:mch_no;type:varchar(32);uniqueIndex:uidx_pay_channel_mch_app_way;index;not null"`
	AppID          string    `gorm:"column:app_id;type:varchar(64);uniqueIndex:uidx_pay_channel_mch_app_way;not null"`
	IfCode         string    `gorm:"column:if_code;type:varchar(32);index;not null"`
	WayCode        string    `gorm:"column:way_code;type:varchar(32);uniqueIndex:uidx_pay_channel_mch_app_way;not null"`
	ChannelMchNo   *string   `gorm:"column:channel_mch_no;type:varchar(64)"`
	ChannelMchName *string   `gorm:"column:channel_mch_name;type:varchar(128)"`
	State     int16  `gorm:"column:state;not null;default:1"`
	Config    []byte `gorm:"column:config;type:jsonb"`
	CreatedAt int64  `gorm:"column:created_at;not null"` // Unix 毫秒
	UpdatedAt int64  `gorm:"column:updated_at;not null"` // Unix 毫秒
}

func (PayChannel) TableName() string { return "payment.pay_channel" }

// PayOrder 支付订单
type PayOrder struct {
	ID              int64      `gorm:"primaryKey;autoIncrement"`
	PayOrderNo      string     `gorm:"column:pay_order_no;type:varchar(64);uniqueIndex;not null"`
	MchNo           string     `gorm:"column:mch_no;type:varchar(32);index;not null"`
	AppID           string     `gorm:"column:app_id;type:varchar(64);not null"`
	IfCode          string     `gorm:"column:if_code;type:varchar(32);not null"`
	WayCode         string     `gorm:"column:way_code;type:varchar(32);not null"`
	Amount          int64      `gorm:"column:amount;not null"`
	Currency        string     `gorm:"column:currency;type:varchar(8);not null;default:CNY"`
	State           int16      `gorm:"column:state;not null;default:0"`
	ChannelOrderNo  *string    `gorm:"column:channel_order_no;type:varchar(128)"`
	ChannelUID      *string    `gorm:"column:channel_uid;type:varchar(128)"`
	Subject         *string    `gorm:"column:subject;type:varchar(256)"`
	Body            *string    `gorm:"column:body;type:varchar(512)"`
	NotifyURL       *string    `gorm:"column:notify_url;type:varchar(512)"`
	ReturnURL       *string    `gorm:"column:return_url;type:varchar(512)"`
	ExpireTime  *int64 `gorm:"column:expire_time"`   // Unix 毫秒
	SuccessTime *int64 `gorm:"column:success_time"` // Unix 毫秒
	CancelTime  *int64 `gorm:"column:cancel_time"`   // Unix 毫秒
	CreatedAt   int64  `gorm:"column:created_at;not null"` // Unix 毫秒
	UpdatedAt   int64  `gorm:"column:updated_at;not null"` // Unix 毫秒
}

func (PayOrder) TableName() string { return "payment.pay_order" }

// OrderRecord 支付订单状态变更记录
type OrderRecord struct {
	ID          int64     `gorm:"primaryKey;autoIncrement"`
	PayOrderNo  string    `gorm:"column:pay_order_no;type:varchar(64);index;not null"`
	EventType   string    `gorm:"column:event_type;type:varchar(32);not null"`
	OldState    *int16    `gorm:"column:old_state"`
	NewState    *int16    `gorm:"column:new_state"`
	Extra     []byte `gorm:"column:extra;type:jsonb"`
	CreatedAt int64  `gorm:"column:created_at;not null"` // Unix 毫秒
}

func (OrderRecord) TableName() string { return "payment.order_record" }

// RefundOrder 退款订单
type RefundOrder struct {
	ID              int64      `gorm:"primaryKey;autoIncrement"`
	RefundOrderNo   string     `gorm:"column:refund_order_no;type:varchar(64);uniqueIndex;not null"`
	PayOrderNo      string     `gorm:"column:pay_order_no;type:varchar(64);index;not null"`
	MchNo           string     `gorm:"column:mch_no;type:varchar(32);index;not null"`
	AppID           string     `gorm:"column:app_id;type:varchar(64);not null"`
	RefundAmount    int64      `gorm:"column:refund_amount;not null"`
	Currency        string     `gorm:"column:currency;type:varchar(8);not null;default:CNY"`
	State           int16      `gorm:"column:state;not null;default:0"`
	ChannelRefundNo *string    `gorm:"column:channel_refund_no;type:varchar(128)"`
	Reason      *string `gorm:"column:reason;type:varchar(256)"`
	ApplyTime   int64   `gorm:"column:apply_time;not null"` // Unix 毫秒
	SuccessTime *int64  `gorm:"column:success_time"`       // Unix 毫秒
	CreatedAt   int64   `gorm:"column:created_at;not null"` // Unix 毫秒
	UpdatedAt   int64   `gorm:"column:updated_at;not null"` // Unix 毫秒
}

func (RefundOrder) TableName() string { return "payment.refund_order" }

// ReconcileBatch 对账批次
type ReconcileBatch struct {
	ID           int64   `gorm:"primaryKey;autoIncrement"`
	BatchNo      string  `gorm:"column:batch_no;type:varchar(64);uniqueIndex;not null"`
	Channel      string  `gorm:"column:channel;type:varchar(32);not null"`
	BillDate     int64   `gorm:"column:bill_date;not null"` // 日期 Unix 毫秒或 YYYYMMDD 整数（按业务约定）
	State        int16   `gorm:"column:state;not null;default:0"`
	TotalCount   int32   `gorm:"column:total_count;not null;default:0"`
	MatchCount   int32   `gorm:"column:match_count;not null;default:0"`
	MistakeCount int32   `gorm:"column:mistake_count;not null;default:0"`
	Remark       *string `gorm:"column:remark;type:varchar(256)"`
	CreatedAt    int64   `gorm:"column:created_at;not null"` // Unix 毫秒
	UpdatedAt    int64   `gorm:"column:updated_at;not null"` // Unix 毫秒
}

func (ReconcileBatch) TableName() string { return "payment.reconcile_batch" }

// ReconcileDetail 对账明细/差错
type ReconcileDetail struct {
	ID              int64     `gorm:"primaryKey;autoIncrement"`
	BatchNo         string    `gorm:"column:batch_no;type:varchar(64);index;not null"`
	PayOrderNo      *string   `gorm:"column:pay_order_no;type:varchar(64)"`
	ChannelOrderNo  *string   `gorm:"column:channel_order_no;type:varchar(128)"`
	MchNo           *string   `gorm:"column:mch_no;type:varchar(32)"`
	Amount          *int64    `gorm:"column:amount"`
	PayState        *int16    `gorm:"column:pay_state"`
	ChannelState    *string   `gorm:"column:channel_state;type:varchar(32)"`
	DiffType  int16   `gorm:"column:diff_type;not null"`
	DiffMsg   *string `gorm:"column:diff_msg;type:varchar(512)"`
	CreatedAt int64   `gorm:"column:created_at;not null"` // Unix 毫秒
}

func (ReconcileDetail) TableName() string { return "payment.reconcile_detail" }

// SettlementRecord 结算记录
type SettlementRecord struct {
	ID           int64      `gorm:"primaryKey;autoIncrement"`
	SettlementNo string     `gorm:"column:settlement_no;type:varchar(64);uniqueIndex;not null"`
	AccType      int16      `gorm:"column:acc_type;not null"`
	AccNo        string     `gorm:"column:acc_no;type:varchar(32);index:idx_settlement_acc;not null"`
	SettlePeriod string     `gorm:"column:settle_period;type:varchar(32);not null"`
	SettleAmount int64      `gorm:"column:settle_amount;not null"`
	FeeAmount    int64      `gorm:"column:fee_amount;not null;default:0"`
	State      int16   `gorm:"column:state;not null;default:0"`
	PayTime    *int64  `gorm:"column:pay_time"` // Unix 毫秒
	PayChannel *string `gorm:"column:pay_channel;type:varchar(64)"`
	PayTradeNo  *string `gorm:"column:pay_trade_no;type:varchar(128)"`
	Remark      *string `gorm:"column:remark;type:varchar(256)"`
	CreatedAt   int64   `gorm:"column:created_at;not null"` // Unix 毫秒
	UpdatedAt   int64   `gorm:"column:updated_at;not null"` // Unix 毫秒
}

func (SettlementRecord) TableName() string { return "payment.settlement_record" }
