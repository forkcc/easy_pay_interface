package com.easypay.api.enums;

/**
 * 支付订单状态枚举
 */
public enum PayOrderStatusEnum {
    INIT((byte) 0, "初始"),
    ING((byte) 1, "支付中"),
    SUCCESS((byte) 2, "支付成功"),
    FAIL((byte) 3, "支付失败"),
    CANCEL((byte) 4, "已撤销"),
    REFUND((byte) 5, "已退款"),
    CLOSED((byte) 6, "订单关闭"),
    EXPIRED((byte) 7, "已过期");

    private final byte code;
    private final String desc;

    PayOrderStatusEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static PayOrderStatusEnum fromCode(byte code) {
        for (PayOrderStatusEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }
}
