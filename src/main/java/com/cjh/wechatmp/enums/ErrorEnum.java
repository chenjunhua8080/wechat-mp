package com.cjh.wechatmp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 接口返回状态枚举
 */
@Getter
@AllArgsConstructor
public enum ErrorEnum {

    OK(200, "ok"),
    FAIL(500, "fail"),
    NOT_FOUND(404, "404"),
    ;

    private int code;
    private String name;

    public static ErrorEnum from(Integer code) {
        if (code == null) {
            return null;
        }
        for (ErrorEnum e : ErrorEnum.values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }

    public static String getNameByCode(Integer code) {
        ErrorEnum e = from(code);
        return e == null ? "" : e.getName();
    }
}
