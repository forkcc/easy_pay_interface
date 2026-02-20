package com.easypay.api.enums;

public enum SettStatusEnum {
    INIT((byte) 0, "初始"),
    ING((byte) 1, "结算中"),
    SUCCESS((byte) 2, "结算成功"),
    FAIL((byte) 3, "结算失败");

    private final byte code;
    private final String desc;

    SettStatusEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static SettStatusEnum fromCode(byte code) {
        for (SettStatusEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }
}
