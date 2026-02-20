package com.easypay.api.enums;

/**
 * 商户通知状态枚举
 */
public enum NotifyStatusEnum {
    INIT((byte) 0, "初始"),
    ING((byte) 1, "通知中"),
    SUCCESS((byte) 2, "通知成功"),
    FAIL((byte) 3, "通知失败");

    private final byte code;
    private final String desc;

    NotifyStatusEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static NotifyStatusEnum fromCode(byte code) {
        for (NotifyStatusEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }
}
