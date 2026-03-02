package rpc

// 以下 DTO 仿照 Jeepay / 龙果支付 的统一下单、退款等接口参数

// UnifiedOrderReq 统一下单请求（仿 Jeepay 支付接口）
type UnifiedOrderReq struct {
	MchNo       string `json:"mchNo"`       // 商户号
	AppId       string `json:"appId"`       // 应用ID
	MchOrderNo  string `json:"mchOrderNo"`  // 商户订单号
	WayCode     string `json:"wayCode"`     // 支付方式，如 wx_native、alipay_wap
	Amount      int64  `json:"amount"`      // 金额，单位分
	Currency    string `json:"currency"`     // 币种，默认 CNY
	Subject     string `json:"subject"`     // 商品标题
	Body        string `json:"body"`        // 商品描述
	NotifyUrl   string `json:"notifyUrl"`    // 异步通知地址
	ReturnUrl   string `json:"returnUrl"`   // 同步跳转地址
	ExpiredSec  int64  `json:"expiredSec"`  // 订单过期秒数，0 表示默认
}

// UnifiedOrderResp 统一下单响应
type UnifiedOrderResp struct {
	PayOrderNo string `json:"payOrderNo"` // 平台支付订单号
	State      int16  `json:"state"`      // 订单状态：0 待支付 1 已支付 2 已关闭
	CodeUrl    string `json:"codeUrl"`    // 扫码支付时二维码链接（如 Native）
	PayData    string `json:"payData"`    // 其它支付方式所需数据（如 JSAPI 调起参数）
}

// RefundReq 退款请求（仿 Jeepay 退款接口）
type RefundReq struct {
	RefundOrderNo string `json:"refundOrderNo"` // 退款单号（商户生成）
	PayOrderNo    string `json:"payOrderNo"`    // 原支付订单号
	MchNo         string `json:"mchNo"`         // 商户号
	RefundAmount  int64  `json:"refundAmount"`  // 退款金额，单位分
	Reason        string `json:"reason"`        // 退款原因
}
