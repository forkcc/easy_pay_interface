-- =============================================
-- 支付基础数据初始化
-- 支付方式 + 支付接口定义
-- =============================================

-- 微信支付方式
INSERT INTO t_pay_way (way_code, way_name, state, remark) VALUES
('WXPAY_JSAPI',  '微信JSAPI支付',    1, '微信公众号内支付'),
('WXPAY_NATIVE', '微信Native支付',   1, '微信扫码支付(商户展示二维码)'),
('WXPAY_H5',     '微信H5支付',       1, '微信外部浏览器H5支付'),
('WXPAY_APP',    '微信APP支付',      1, '微信APP内支付'),
('WXPAY_MINI',   '微信小程序支付',    1, '微信小程序内支付');

-- 支付宝支付方式
INSERT INTO t_pay_way (way_code, way_name, state, remark) VALUES
('ALIPAY_PC',    '支付宝PC支付',     1, '支付宝PC网页支付'),
('ALIPAY_WAP',   '支付宝WAP支付',    1, '支付宝手机网页支付'),
('ALIPAY_APP',   '支付宝APP支付',    1, '支付宝APP内支付'),
('ALIPAY_QR',    '支付宝扫码支付',    1, '支付宝扫码支付(商户展示二维码)');

-- 银联支付方式
INSERT INTO t_pay_way (way_code, way_name, state, remark) VALUES
('UNIONPAY_QR',  '银联二维码支付',    1, '银联二维码主被扫'),
('UNIONPAY_PC',  '银联PC网关支付',   1, '银联PC网关跳转支付');

-- 支付接口定义
INSERT INTO t_pay_interface_define (if_code, if_name, if_type, state, remark, config_info) VALUES
('wxpay',    '微信支付官方',  1, 1, '微信支付官方直连接口',
 '{"mchId":"","appId":"","appSecret":"","apiKey":"","apiV3Key":"","serialNo":"","cert":""}'),
('alipay',   '支付宝官方',    1, 1, '支付宝官方直连接口',
 '{"appId":"","privateKey":"","alipayPublicKey":"","signType":"RSA2"}'),
('unionpay', '银联支付',      1, 1, '银联在线支付接口',
 '{"merId":"","certPath":"","certPwd":""}');
