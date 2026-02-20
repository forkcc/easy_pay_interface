package com.easypay.api.enums;

/**
 * 退款订单状态枚举
 */
public enum RefundStatusEnum {
    INIT((byte) 0, "初始"),
    ING((byte) 1, "退款中"),
    SUCCESS((byte) 2, "退款成功"),
    FAIL((byte) 3, "退款失败"),
    CLOSED((byte) 4, "退款关闭");

    private final byte code;
    private final String desc;

    RefundStatusEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static RefundStatusEnum fromCode(byte code) {
        for (RefundStatusEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }
}
