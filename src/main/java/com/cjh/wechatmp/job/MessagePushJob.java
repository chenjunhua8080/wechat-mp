package com.cjh.wechatmp.job;

import com.cjh.wechatmp.dao.BindFarmDao;
import com.cjh.wechatmp.dao.ReportDao;
import com.cjh.wechatmp.dao.UserDao;
import com.cjh.wechatmp.enums.PlatformEnum;
import com.cjh.wechatmp.farm.FarmService;
import com.cjh.wechatmp.feign.CloudFeignClient;
import com.cjh.wechatmp.message.push.MessagePushService;
import com.cjh.wechatmp.po.UserPO;
import com.cjh.wechatmp.service.ReportService;
import com.cjh.wechatmp.service.UserService;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@EnableScheduling
@Slf4j
public class MessagePushJob {

    private MessagePushService pushService;
    private FarmService farmService;
    private UserService userService;
    private ReportService reportService;
    private CloudFeignClient cloudFeignClient;
    private BindFarmDao bindFarmDao;
    private UserDao userDao;
    private ReportDao reportDao;

    /**
     * 推送文字消息
     */
//    @Scheduled(cron = "${job.messagePush}")
    public void signForFarmPushTextByOpenId() {
        log.info("messagePush job: {}", new Date());
        List<UserPO> list = userService.list();
        for (UserPO userPO : list) {
            String openId = userPO.getOpenId();
            String farmLog = farmService.getTodayLog(openId, PlatformEnum.JD_FARM.getCode(), false);
            log.info("尝试推送文本消息: openId -> {}, text -> {}", openId, farmLog);
            String resp = pushService.pushTextByOpenId(farmLog);
            log.info("推送结果: {}", resp);
        }
    }

    /**
     * 定时推送模板消息[京东-农场]
     */
    @Scheduled(cron = "${job.tempPush}")
    public void signForFarm() {
        log.info("tempPush job: {}", new Date());
        List<UserPO> list = userService.list();
        for (UserPO userPO : list) {
            String openId = userPO.getOpenId();
            if (notBind(openId, PlatformEnum.JD_FARM.getCode())) {
                continue;
            }
            String farmLog = farmService.getTodayLog(openId, PlatformEnum.JD_FARM.getCode(), false);
            log.info("尝试推送模板消息: openId -> {}, text -> {}", openId, farmLog);
            String resp = pushService.pushJobMsg(openId, farmLog);
            log.info("推送结果: {}", resp);
        }
    }

    /**
     * 定时推送模板消息[京东-宠物]
     */
    @Scheduled(cron = "${job.petsPush}")
    public void petsPush() {
        log.info("petsPush job: {}", new Date());
        List<UserPO> list = userService.list();
        for (UserPO userPO : list) {
            String openId = userPO.getOpenId();
            if (notBind(openId, PlatformEnum.JD_PETS.getCode())) {
                continue;
            }
            String farmLog = farmService.getTodayLog(openId, PlatformEnum.JD_PETS.getCode(), false);
            log.info("尝试推送模板消息: openId -> {}, text -> {}", openId, farmLog);
            String resp = pushService.pushJobMsg(openId, farmLog);
            log.info("推送结果: {}", resp);
        }
    }

    /**
     * 定时推送模板消息[自我报告]
     */
    @Scheduled(cron = "${job.reportPush}")
    public void reportPush() {
        log.info("reportPush job: {}", new Date());
        List<UserPO> list = userService.list();
        for (UserPO userPO : list) {
            String openId = userPO.getOpenId();
            if (reportDao.countByUser(openId) == 0) {
                continue;
            }
            String reportText = reportService.getReportText(openId);
            log.info("尝试推送模板消息: openId -> {}, text -> {}", openId, reportText);
            String resp = pushService.pushReportMsg(openId, reportText);
            log.info("推送结果: {}", resp);
        }
    }

    /**
     * 定时推送模板消息[京东-618收集金币]
     */
//    @Scheduled(cron = "${job.jdPush}")
    public void jdPush() {
        log.info("jdPush job: {}", new Date());
        List<UserPO> list = userService.list();
        for (UserPO userPO : list) {
            String openId = userPO.getOpenId();
            if (notBind(openId, PlatformEnum.JD_CAKE.getCode())) {
                continue;
            }
            String farmLog = cloudFeignClient.getHomeData(openId);
            log.info("尝试推送模板消息: openId -> {}, text -> {}", openId, farmLog);
            String resp = pushService.pushJobMsg(openId, farmLog);
            log.info("推送结果: {}", resp);
        }
    }

    /**
     * 定时推送模板消息[中国银行-签到]
     */
    @Scheduled(cron = "${job.bankChinaPush}")
    public void bankChinaPush() {
        log.info("bankChinaPush job: {}", new Date());
        List<UserPO> list = userService.list();
        for (UserPO userPO : list) {
            String openId = userPO.getOpenId();
            if (notBind(openId, PlatformEnum.BANK_CHINA.getCode())) {
                continue;
            }
            String farmLog = cloudFeignClient.getBankChinaInfo(openId);
            log.info("尝试推送模板消息: openId -> {}, text -> {}", openId, farmLog);
            String resp = pushService.pushJobMsg(openId, farmLog);
            log.info("推送结果: {}", resp);
        }
    }

    /**
     * 查询是否绑定
     */
    private boolean notBind(String openId, Integer platformType) {
        UserPO user = userDao.selectByOpenId(openId);
        return bindFarmDao.getBindUser(user.getId(), platformType) == null;
    }

}
