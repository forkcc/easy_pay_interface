package com.easypay.api.enums;

public enum SysUserTypeEnum {
    MGR((byte) 1, "运营"),
    MCH((byte) 2, "商户"),
    AGENT((byte) 3, "代理商");

    private final byte code;
    private final String desc;

    SysUserTypeEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static SysUserTypeEnum fromCode(byte code) {
        for (SysUserTypeEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }
}
