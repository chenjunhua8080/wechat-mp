package com.cjh.wechatmp.menu;

import com.cjh.wechatmp.api.WxApi;
import com.cjh.wechatmp.exception.WxErrorEntity;
import com.cjh.wechatmp.token.TokenService;
import com.cjh.wechatmp.util.JsonUtil;
import com.cjh.wechatmp.util.RestTemplateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 菜单服务
 */
@AllArgsConstructor
@Component
@Slf4j
public class MenuService {

    private TokenService tokenService;

    private MenuEntity menuEntity;

    public String create() {
        String url = WxApi.MENU_CREATE.replace("ACCESS_TOKEN", tokenService.getBaseToken());
        String resp = RestTemplateUtil.doPost(url, JsonUtil.java2Json2_(menuEntity));
        WxErrorEntity errorEntity = JsonUtil.json2java(resp, WxErrorEntity.class);
        if (errorEntity.getErrcode() == 0) {
            log.info("创建菜单成功: {}", menuEntity);
        } else {
            log.info("创建菜单失败: {}", resp);
        }
        return resp;
    }

    public String delete() {
        String url = WxApi.MENU_DELETE.replace("ACCESS_TOKEN", tokenService.getBaseToken());
        String resp = RestTemplateUtil.doGet(url);
        WxErrorEntity errorEntity = JsonUtil.json2java(resp, WxErrorEntity.class);
        if (errorEntity.getErrcode() == 0) {
            log.info("删除菜单成功");
        } else {
            log.info("删除菜单失败: {}", resp);
        }
        return resp;
    }

}
