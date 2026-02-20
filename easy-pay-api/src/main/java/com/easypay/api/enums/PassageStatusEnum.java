package com.easypay.api.enums;

public enum PassageStatusEnum {
    ACTIVE((byte) 1, "正常"),
    DISABLED((byte) 0, "停用");

    private final byte code;
    private final String desc;

    PassageStatusEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static PassageStatusEnum fromCode(byte code) {
        for (PassageStatusEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }
}
