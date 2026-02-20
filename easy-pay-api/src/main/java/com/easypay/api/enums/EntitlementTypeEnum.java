package com.easypay.api.enums;

/**
 * 权限资源类型枚举
 */
public enum EntitlementTypeEnum {
    MENU((byte) 1, "菜单"),
    BUTTON((byte) 2, "按钮");

    private final byte code;
    private final String desc;

    EntitlementTypeEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static EntitlementTypeEnum fromCode(byte code) {
        for (EntitlementTypeEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }
}
