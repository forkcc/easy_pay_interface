package com.easypay.api.enums;

/**
 * 账户变动类型枚举
 */
public enum AccountChangeTypeEnum {
    INCOME((byte) 1, "收入"),
    EXPENSE((byte) 2, "支出"),
    FREEZE((byte) 3, "冻结"),
    UNFREEZE((byte) 4, "解冻"),
    SETTLE((byte) 5, "结算");

    private final byte code;
    private final String desc;

    AccountChangeTypeEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static AccountChangeTypeEnum fromCode(byte code) {
        for (AccountChangeTypeEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }
}
