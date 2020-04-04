package com.cjh.wechatmp.controller;

import com.cjh.wechatmp.farm.FarmService;
import com.cjh.wechatmp.message.push.MessagePushService;
import com.cjh.wechatmp.po.UserPO;
import com.cjh.wechatmp.service.ReportService;
import com.cjh.wechatmp.service.UserService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/message")
public class MessagePushController {

    private MessagePushService pushService;
    private ReportService reportService;
    private FarmService farmService;
    private UserService userService;

    @GetMapping("/textPush")
    public String textPush(String body) {
        log.info("****************推送消息**********************");
        String resp = pushService.pushTextByOpenId(body);
        log.info("****************推送消息结束*******************");
        return resp;
    }

    @GetMapping("/tempPush")
    public String tempPush(@RequestParam("openId") String openId, @RequestParam("body") String body) {
        log.info("****************推送消息**********************");
        String resp = pushService.pushTempByOpenId(openId, body);
        log.info("****************推送消息结束*******************");
        return String.valueOf(resp);
    }

    /**
     * 手动推送
     */
    @GetMapping("/push")
    public String push(@RequestParam("type") Integer type, @RequestParam("openId") String openId) {
        String resp;
        log.info("****************推送消息**********************");
        if (type == 1) {
            if (openId == null) {
                List<UserPO> list = userService.list();
                for (UserPO userPO : list) {
                    openId = userPO.getOpenId();
                    String farmLog = farmService.getTodayFarmLog(openId);
                    pushService.pushTempByOpenId(openId, farmLog);
                }
                resp = String.valueOf(list);
            } else {
                String farmLog = farmService.getTodayFarmLog(openId);
                pushService.pushTempByOpenId(openId, farmLog);
                resp = farmLog;
            }
        } else if (type == 2) {
            if (openId == null) {
                List<UserPO> list = userService.list();
                for (UserPO userPO : list) {
                    openId = userPO.getOpenId();
                    String reportText = reportService.getReportText(openId);
                    pushService.pushReport(openId, reportText);
                }
                resp = String.valueOf(list);
            } else {
                String reportText = reportService.getReportText(openId);
                pushService.pushReport(openId, reportText);
                resp = reportText;
            }
        } else {
            resp = "type: 1农场, 2报告";
        }
        log.info("****************推送消息结束*******************");
        return String.valueOf(resp);
    }

}
