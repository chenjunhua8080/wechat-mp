package com.cjh.wechatmp.controller;

import com.cjh.wechatmp.sign.SignEntity;
import com.cjh.wechatmp.sign.SignService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/message")
public class SignController {

    private SignService signService;

    @GetMapping("/in")
    public String sign(SignEntity signEntity) {
        log.info("*******************收到微信服务器校验请求*******************");
        log.info("参数：{}", signEntity.toString());
        String sign = signService.sign(signEntity);
        log.info("结果：{}", sign != null);
        log.info("**************************校验结束**************************");
        return sign;
    }

}
