package com.easypay.provider.config;

import java.time.Duration;

public final class CacheConstants {

    private CacheConstants() {}

    public static final String MCH_INFO       = "mch:info";
    public static final String MCH_APP        = "mch:app";
    public static final String PAY_WAY        = "pay:way";
    public static final String PAY_WAY_LIST   = "pay:way:list";
    public static final String PAY_PASSAGE    = "pay:passage";
    public static final String PAY_IF_DEFINE  = "pay:if:define";
    public static final String PAY_IF_CONFIG  = "pay:if:config";
    public static final String MCH_PASSAGE    = "mch:passage";

    public static final Duration TTL_SHORT  = Duration.ofMinutes(10);
    public static final Duration TTL_MEDIUM = Duration.ofMinutes(30);
    public static final Duration TTL_LONG   = Duration.ofHours(2);
}
