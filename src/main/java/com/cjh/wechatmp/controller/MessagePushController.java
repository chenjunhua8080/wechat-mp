package com.cjh.wechatmp.controller;

import com.cjh.wechatmp.farm.FarmService;
import com.cjh.wechatmp.message.push.MessagePushService;
import com.google.common.collect.Lists;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/message")
public class MessagePushController {

    private MessagePushService pushService;

    private FarmService farmService;

    @GetMapping("/getLog")
    public String textPush(String openId, int platform, boolean returnNull) {
        return farmService.getTodayLog(openId, platform, returnNull);
    }

    @GetMapping("/textPush")
    public String textPush(String body) {
        log.info("****************推送消息**********************");
        String resp = pushService.pushTextByOpenId(Lists.newArrayList(), body);
        log.info("****************推送消息结束*******************");
        return resp;
    }

    @GetMapping("/tempPush")
    public String tempPush(@RequestParam("openId") String openId, @RequestParam("body") String body) {
        log.info("****************推送消息**********************");
        String resp = pushService.pushJobMsg(openId, body);
        log.info("****************推送消息结束*******************");
        return String.valueOf(resp);
    }

    @GetMapping("/pushErrorMsg")
    public String pushErrorMsg(@RequestParam("openId") String openId, @RequestParam("body") String body) {
        log.info("****************推送消息**********************");
        String resp = pushService.pushErrorMsg(openId, body);
        log.info("****************推送消息结束*******************");
        return String.valueOf(resp);
    }

    @PostMapping("/pushResumeMsg")
    public String pushResumeMsg(@RequestBody Map<String, Object> map) {
        log.info("****************推送消息**********************");
        String resp = pushService.pushResumeMsg(
            String.valueOf(map.get("openId")),
            String.valueOf(map.get("body")),
            String.valueOf(map.get("link"))
        );
        log.info("****************推送消息结束*******************");
        return String.valueOf(resp);
    }

}
