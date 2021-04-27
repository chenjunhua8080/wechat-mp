package com.cjh.wechatmp.handler;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.cjh.wechatmp.annotation.MessageProcessor;
import com.cjh.wechatmp.avatar.AvatarService;
import com.cjh.wechatmp.boss.BossService;
import com.cjh.wechatmp.enums.InstructsEnum;
import com.cjh.wechatmp.farm.FarmService;
import com.cjh.wechatmp.juhe.JuHeService;
import com.cjh.wechatmp.message.BaseMessage;
import com.cjh.wechatmp.message.MessageUtil;
import com.cjh.wechatmp.message.handler.AbstractMessageHandler;
import com.cjh.wechatmp.message.in.TextInMessage;
import com.cjh.wechatmp.po.UserPO;
import com.cjh.wechatmp.redis.RedisConstant;
import com.cjh.wechatmp.redis.RedisService;
import com.cjh.wechatmp.service.ReportService;
import com.cjh.wechatmp.service.UserService;
import com.cjh.wechatmp.util.ByteUtil;
import java.text.ParseException;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

/**
 * 文本消息处理器
 */
@MessageProcessor
@AllArgsConstructor
@Component
@Slf4j
public class TextMessageHandler extends AbstractMessageHandler {

    private FarmService farmService;
    private JuHeService juHeService;
    private AvatarService avatarService;
    private ReportService reportService;
    private RedisService redisService;
    private UserService userService;
    private BossService bossService;

    @Override
    public BaseMessage doHandle(BaseMessage inMessage) {
        //注册
        String openId = inMessage.getFromUserName();
        UserPO userPO = userService.getByOpenId(openId);
        if (userPO == null) {
            userPO = new UserPO();
            userPO.setOpenId(openId);
            userService.save(userPO);
        }

        TextInMessage textInMessage = (TextInMessage) inMessage;
        String content = textInMessage.getContent();
        String result = null;

        if ("help".equals(content) || "home".equals(content) || InstructsEnum.Instruct10.getCode().toString()
            .equals(content)) {
            //清除旧指令
            redisService.getLastInstruct(openId, true);
            result = InstructsEnum.getInstructs(0);
        } else if ("0".equals(content)) {
            //清除旧指令
            redisService.getLastInstruct(openId, true);
            reportService.add(openId);
            result = reportService.getReportText(openId);
        }

        //InstructsEnum
        String lastInstruct = redisService.getLastInstruct(openId, false);
        log.info("#### lastInstruct: {} ####", lastInstruct);
        if (lastInstruct == null && InstructsEnum.getInstructs(0).contains("【" + content + "】")) {
            result = InstructsEnum.getInstructs(Integer.parseInt(content));
            if (result != null) {
                redisService.setLastInstruct(openId, content);
            }
        }

        //report
        if (result == null && lastInstruct == null) {
            if (InstructsEnum.Instruct1.getCode().toString().equals(content)) {
                result = reportService.getReportText(openId);
            }
        }

        //农场业务
        if (result == null) {
            result = farmService.handleMessage(textInMessage);
        }

        //聚合api业务
        if (result == null) {
            result = juHeService.handleMessage(textInMessage);
        }

        //头像业务
        if (result == null) {
            BaseMessage baseMessage = avatarService.handleMessage(textInMessage);
            if (baseMessage != null) {
                return baseMessage;
            }
        }

        //520
        Date now = new Date();
        if (result == null) {
            if (InstructsEnum.Instruct520.getCode().toString().equals(content)) {
                String dateStr = redisService.get("date:520");
                DateTime beginDate = DateUtil.parseDateTime(dateStr);
                long year = DateUtil.betweenYear(beginDate, now, false);
                long month = DateUtil.betweenMonth(beginDate, now, true)%12;
                long day = DateUtil.thisDayOfMonth();
                String yearStr = year == 0 ? "" : String.format("%s年", year);
                result = String.format("我们在一起已经: %s%s个月%s天了 啦啦啦~", yearStr, month, day);
            } else if ("520:set".equals(content)) {
                redisService.setLastInstruct(openId, content);
                result = "请输入日期";
            }
        }
        if (result == null && "520:set".equals(lastInstruct)) {
            try {
                Date date = DateUtils.parseDate(content, "yyyy-MM-dd HH:mm:ss");
                if (date.after(now)){
                    result = "请输出有效的日期！";
                }else {
                    redisService.getLastInstruct(openId, true);
                    redisService.set("date:520", content, RedisConstant.EXIST_FOREVER);
                    result = "设置成功！";
                    long year = DateUtil.betweenYear(date, now, false);
                    long month = DateUtil.betweenMonth(date, now, true)%12;
                    long day = DateUtil.thisDayOfMonth();
                    String yearStr = year == 0 ? "" : String.format("%s年", year);
                    result += String.format("我们在一起已经: %s%s个月%s天了 啦啦啦~", yearStr, month, day);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                result = "请输出正确日期！";
            }
        }

        //BOSS业务
        if (result == null) {
            result = bossService.handleMessage(textInMessage);
        }

        //原样返回
        if (result == null) {
            result = content;
        }

        //处理字符过长
        result = ByteUtil.limit2048byte(result);

        return MessageUtil.buildTextOutMessage(textInMessage, result);
    }

    public static void main(String[] args) {
        String result;
        Date now =new Date();
        DateTime beginDate = DateUtil.parseDateTime("2022-10-18 00:00:00");
        long year = DateUtil.betweenYear(beginDate, now, false);
        long month = DateUtil.betweenMonth(beginDate, now, true)%12;
        long day = DateUtil.thisDayOfMonth();
        String yearStr = year == 0 ? "" : String.format("%s年", year);
        result = String.format("我们在一起已经: %s%s个月%s天了 啦啦啦~", yearStr, month, day);
        System.out.println(result);
    }

}
