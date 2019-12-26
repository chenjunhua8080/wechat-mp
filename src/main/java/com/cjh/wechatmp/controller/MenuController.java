package com.cjh.wechatmp.controller;

import com.cjh.wechatmp.menu.MenuService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/menu")
public class MenuController {

    private MenuService menuService;

    @GetMapping("/create")
    public String create() {
        log.info("****************创建菜单**********************");
        menuService.delete();
        String resp = menuService.create();
        log.info("****************创建菜单结束*******************");
        return resp;
    }

}
