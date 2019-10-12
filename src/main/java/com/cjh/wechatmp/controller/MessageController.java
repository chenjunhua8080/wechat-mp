package com.cjh.wechatmp.controller;

import com.cjh.wechatmp.sign.SignEntity;
import com.cjh.wechatmp.sign.SignService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/message")
public class MessageController {

    private SignService signService;

    @PostMapping("/in")
    public String sign(SignEntity signEntity) {
        log.info("*******************收到微信服务器消息*******************");
        log.info("参数：{}", signEntity.toString());
        String sign = signService.sign(signEntity);
        log.info("结果：{}", sign != null);
        log.info("**********************消息处理结束**********************");
        return sign;
    }

}
