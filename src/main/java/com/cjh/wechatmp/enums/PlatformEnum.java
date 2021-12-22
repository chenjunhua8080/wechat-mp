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
    BANK_CHINA(2, "中国银行"),
    JD_FARM(3, "京东-农场"),
    JD_PETS(4, "京东-宠物"),
    BOSS(5, "BOSS_账号"),
    BOSS_EMAIL(6, "BOSS_邮箱"),
    UHOME(7, "有家"),
    TONGCHENG(8, "同程"),
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
