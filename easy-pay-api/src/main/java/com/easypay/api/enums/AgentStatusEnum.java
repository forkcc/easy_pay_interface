package com.easypay.api.enums;

public enum AgentStatusEnum {
    ACTIVE((byte) 1, "正常"),
    DISABLED((byte) 0, "停用");

    private final byte code;
    private final String desc;

    AgentStatusEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static AgentStatusEnum fromCode(byte code) {
        for (AgentStatusEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }
}
