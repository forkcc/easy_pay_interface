package com.easypay.api.enums;

public enum TransferStatusEnum {
    INIT((byte) 0, "初始"),
    ING((byte) 1, "转账中"),
    SUCCESS((byte) 2, "转账成功"),
    FAIL((byte) 3, "转账失败"),
    CLOSED((byte) 4, "已关闭");

    private final byte code;
    private final String desc;

    TransferStatusEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static TransferStatusEnum fromCode(byte code) {
        for (TransferStatusEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }
}
