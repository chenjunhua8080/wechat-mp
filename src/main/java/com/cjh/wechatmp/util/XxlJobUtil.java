package com.cjh.wechatmp.util;

import cn.hutool.extra.spring.SpringUtil;
import com.cjh.wechatmp.message.push.MessagePushService;
import com.xxl.job.core.context.XxlJobHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * @author cjh
 * @date 2021/12/17
 */
@Slf4j
public class XxlJobUtil {

    /**
     * 本地输出log && xxl-job输出log
     *
     * @param msg msg
     */
    public static void showLog(String msg) {
        log.info(msg);
        XxlJobHelper.log(msg);
    }

    /**
     * 本地输出log && xxl-job输出log
     *
     * @param msg msg
     */
    public static void showErrorLog(String msg) {
        log.error(msg);
        XxlJobHelper.log(msg);
    }

    /**
     * 本地输出log && xxl-job输出log && 推送微信
     *
     * @param msg    msg
     * @param openId openId
     */
    public static void showErrorLog(String msg, String openId) {
        log.error(msg);
        XxlJobHelper.log(msg);
        MessagePushService pushService = SpringUtil.getBean(MessagePushService.class);
        if (pushService == null) {
            log.error("推送失败，获取pushService为空");
            return;
        }
        pushService.pushJobMsg(openId, msg);
        XxlJobHelper.log("推送消息到微信：", openId);
    }

}
