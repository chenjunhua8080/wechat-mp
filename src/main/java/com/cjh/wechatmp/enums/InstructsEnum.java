package com.cjh.wechatmp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 指令枚举
 */
@Getter
@AllArgsConstructor
public enum InstructsEnum {

    Instruct1(1, 0, "报告"),
    Instruct2(2, 0, "京东#618"),
    Instruct21(1, 2, "绑定"),
    Instruct22(2, 2, "当前级别"),
    Instruct23(3, 2, "总计收集金币"),
    Instruct24(4, 2, "当前日志"),
    Instruct3(3, 0, "京东#农场"),
    Instruct31(1, 3, "绑定"),
    Instruct32(2, 3, "当前日志"),
    Instruct4(4, 0, "中国银行"),
    Instruct41(1, 4, "绑定"),
    Instruct42(2, 4, "当前日志"),
    Instruct5(5, 0, "天气"),
    Instruct51(1, 5, "广州"),
    Instruct52(2, 5, "其他回复城市名: 如广州"),
    Instruct6(6, 0, "星座运势"),
    Instruct61(1, 6, "白羊座"),
    Instruct62(2, 6, "金牛座"),
    Instruct63(3, 6, "双子座"),
    Instruct64(4, 6, "巨蟹座"),
    Instruct65(5, 6, "狮子座"),
    Instruct66(6, 6, "处女座"),
    Instruct67(7, 6, "天秤座"),
    Instruct68(8, 6, "天蝎座"),
    Instruct69(9, 6, "射手座"),
    Instruct70(10, 6, "摩羯座"),
    Instruct71(11, 6, "水瓶座"),
    Instruct72(12, 6, "双鱼座"),
    Instruct7(7, 0, "笑话"),
    Instruct8(8, 0, "历史上的今天"),
    Instruct9(9, 0, "头像"),
    Instruct91(1, 9, "单个头像"),
    Instruct92(2, 9, "图文列表"),
    Instruct10(10, 0, "回复home，回到主菜单"),
    Instruct99(99, 0, "温馨提示：连续对话场景30秒内有效~");

    private Integer code;
    private Integer pid;
    private String name;

    public static InstructsEnum from(Integer code, Integer pid) {
        if (code == null || pid == null) {
            return null;
        }
        for (InstructsEnum e : InstructsEnum.values()) {
            if (e.code.equals(code) && e.pid.equals(pid)) {
                return e;
            }
        }
        return null;
    }

    public static String getNameByCode(Integer code, Integer pid) {
        InstructsEnum e = from(code, pid);
        return e == null ? "" : e.getName();
    }

    public static String getInstructs(int pid) {
        StringBuilder result = new StringBuilder();
        if (pid == 0) {
            result.append("功能清单：\n");
        }
        InstructsEnum[] values = InstructsEnum.values();
        for (InstructsEnum value : values) {
            if (!value.pid.equals(pid)) {
                continue;
            }
            result.append("【").append(value.getCode()).append("】").append(value.getName()).append("\n");
        }
        return "".equals(result.toString()) ? null : result.toString();
    }

    public static void main(String[] args) {
        System.out.println(getInstructs(7));
    }

}
