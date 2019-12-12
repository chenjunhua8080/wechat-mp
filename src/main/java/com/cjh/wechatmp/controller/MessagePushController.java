package com.cjh.wechatmp.controller;

import com.cjh.wechatmp.message.push.MessagePushService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/message")
public class MessagePushController {

    private MessagePushService pushService;

    @GetMapping("/push")
    public String create(String body) {
        log.info("****************推送消息**********************");
        String resp = pushService.pushTextByOpenId(body);
        log.info("****************推送消息结束*******************");
        return resp;
    }

}
