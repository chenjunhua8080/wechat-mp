package com.cjh.wechatmp.job;

import cn.hutool.core.collection.CollectionUtil;
import com.cjh.wechatmp.dao.BindFarmDao;
import com.cjh.wechatmp.dao.UserDao;
import com.cjh.wechatmp.enums.PlatformEnum;
import com.cjh.wechatmp.farm.FarmService;
import com.cjh.wechatmp.message.push.MessagePushService;
import com.cjh.wechatmp.po.UserPO;
import com.cjh.wechatmp.service.UserService;
import com.xxl.job.core.handler.annotation.XxlJob;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Slf4j
public class MessagePushJob {

    private MessagePushService pushService;
    private FarmService farmService;
    private UserService userService;
    private BindFarmDao bindFarmDao;
    private UserDao userDao;

    /**
     * 推送文字消息
     */
    @XxlJob("job.push.text")
    public void pushText() {
        log.info("messagePush job: {}", new Date());
        List<UserPO> list = userService.list();
        if (CollectionUtil.isNotEmpty(list)) {
            List<String> openIds = list.stream().map(UserPO::getOpenId).collect(Collectors.toList());
            log.info("尝试推送文本消息: openId -> {}, text -> {}", openIds, "准备点外卖咯<(*￣▽￣*)/");
            String resp = pushService.pushTextByOpenId(openIds, "准备点外卖咯<(*￣▽￣*)/");
            log.info("推送结果: {}", resp);
        }
    }

    /**
     * 定时推送模板消息[京东-农场]
     */
    @XxlJob("job.push.farm.log")
    public void pushFarmLog() {
        log.info("pushFarmLog job: {}", new Date());
        List<UserPO> list = userService.list();
        for (UserPO user : list) {
            String openId = user.getOpenId();
            if (notBind(openId, PlatformEnum.JD_FARM.getCode())) {
                continue;
            }
            String farmLog = farmService.getTodayLog(openId, PlatformEnum.JD_FARM.getCode(), false);
            log.info("尝试推送模板消息: openId -> {}, text -> {}", openId, farmLog);
//            String resp = pushService.pushJobMsg(openId, farmLog);
//            log.info("推送结果: {}", resp);
        }
    }

    /**
     * 定时推送模板消息[京东-宠物]
     */
    @XxlJob("job.push.pets.log")
    public void pushPetsLog() {
        log.info("petsPush job: {}", new Date());
        List<UserPO> list = userService.list();
        for (UserPO user : list) {
            String openId = user.getOpenId();
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
     * 定时推送模板消息[同程-水滴]
     */
    @XxlJob("job.push.tongcheng.log")
    public void pushTongchengLog() {
        log.info("petsPush job: {}", new Date());
        List<UserPO> list = userService.list();
        for (UserPO user : list) {
            String openId = user.getOpenId();
            String farmLog = farmService.getTodayLog(openId, PlatformEnum.TONGCHENG.getCode(), false);
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
