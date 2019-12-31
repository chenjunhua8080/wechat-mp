package com.cjh.wechatmp.job;

import com.cjh.wechatmp.farm.FarmService;
import com.cjh.wechatmp.message.push.MessagePushService;
import com.cjh.wechatmp.po.UserPO;
import com.cjh.wechatmp.service.UserService;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RefreshScope
@Component
@EnableScheduling
@Slf4j
public class MessagePushJob {

    @Autowired
    private MessagePushService pushService;
    @Autowired
    private FarmService farmService;
    @Autowired
    private UserService userService;

    @Value("${job.messagePush.enable}")
    private Boolean messagePushEnable;
    @Value("${job.tempPush.enable}")
    private Boolean tempPushEnable;

    @Scheduled(cron = "${job.test.cron}")
    public void test() {
        System.out.println("test job: " + new Date());
    }

    @Scheduled(cron = "${job.messagePush.cron}")
    public void signForFarmPushTextByOpenId() {
        if (messagePushEnable) {
            List<UserPO> list = userService.list();
            for (UserPO userPO : list) {
                String openId = userPO.getOpenId();
                String farmLog = farmService.getTodayFarmLog(openId);
                log.info("尝试推送文本消息: openId -> {}, text -> {}", openId, farmLog);
                String resp = pushService.pushTextByOpenId(farmLog);
                log.info("推送结果: {}", resp);
            }
        }
    }

    @Scheduled(cron = "${job.tempPush.cron}")
    public void signForFarm() {
        if (tempPushEnable) {
            List<UserPO> list = userService.list();
            for (UserPO userPO : list) {
                String openId = userPO.getOpenId();
                String farmLog = farmService.getTodayFarmLog(openId);
                log.info("尝试推送模板消息: openId -> {}, text -> {}", openId, farmLog);
                String resp = pushService.pushTempByOpenId(openId, farmLog);
                log.info("推送结果: {}", resp);
            }
        }
    }

}
