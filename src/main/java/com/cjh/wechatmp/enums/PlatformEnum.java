package com.cjh.wechatmp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 平台类型枚举
 */
@Getter
@AllArgsConstructor
public enum PlatformEnum {

    JD_CAKE(1, "京东-618蛋糕"),
    JD_FARM(3, "京东-农场"),
    BANK_CHINA(2, "中国银行"),
    ;

    private int code;
    private String name;

    public static PlatformEnum from(Integer code) {
        if (code == null) {
            return null;
        }
        for (PlatformEnum e : PlatformEnum.values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }

    public static String getNameByCode(Integer code) {
        PlatformEnum e = from(code);
        return e == null ? "" : e.getName();
    }
}
