package com.easypay.api.enums;

public enum MchStatusEnum {
    ACTIVE((byte) 1, "正常"),
    DISABLED((byte) 0, "停用");

    private final byte code;
    private final String desc;

    MchStatusEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static MchStatusEnum fromCode(byte code) {
        for (MchStatusEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }
}
