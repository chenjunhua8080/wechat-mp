package com.cjh.wechatmp.api;

import lombok.Data;

@Data
public class WxApi {

    /**
     * 获取基础支持access_token
     */
    public static final String BASE_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    /**
     * 创建菜单
     */
    public static final String MENU_CREATE = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    /**
     * 删除菜单
     */
    public static final String MENU_DELETE = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

}
