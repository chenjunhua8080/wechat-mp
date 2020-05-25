package com.cjh.wechatmp.handler;

import com.cjh.wechatmp.annotation.MessageProcessor;
import com.cjh.wechatmp.avatar.AvatarService;
import com.cjh.wechatmp.enums.InstructsEnum;
import com.cjh.wechatmp.farm.FarmService;
import com.cjh.wechatmp.juhe.JuHeService;
import com.cjh.wechatmp.message.BaseMessage;
import com.cjh.wechatmp.message.MessageUtil;
import com.cjh.wechatmp.message.handler.AbstractMessageHandler;
import com.cjh.wechatmp.message.in.TextInMessage;
import com.cjh.wechatmp.po.UserPO;
import com.cjh.wechatmp.redis.RedisService;
import com.cjh.wechatmp.service.ReportService;
import com.cjh.wechatmp.service.UserService;
import com.cjh.wechatmp.util.ByteUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public BaseMessage doHandle(BaseMessage inMessage) {
        //注册
        String userId = inMessage.getFromUserName();
        UserPO userPO = userService.getByOpenId(userId);
        if (userPO == null) {
            userPO = new UserPO();
            userPO.setOpenId(userId);
            userService.save(userPO);
        }

        TextInMessage textInMessage = (TextInMessage) inMessage;
        String content = textInMessage.getContent();
        String result = null;

        if ("help".equals(content) || InstructsEnum.Instruct10.getCode().toString().equals(content)) {
            result = InstructsEnum.getInstructs(0);
        } else if ("0".equals(content)) {
            String openId = userId;
            reportService.add(openId);
            result = reportService.getReportText(openId);
        }

        //InstructsEnum
        String lastInstruct = redisService.getLastInstruct(textInMessage.getFromUserName(), false);
        log.info("#### lastInstruct: {} ####", lastInstruct);
        if (lastInstruct == null && InstructsEnum.getInstructs(0).contains("【" + content + "】")) {
            result = InstructsEnum.getInstructs(Integer.parseInt(content));
            if (result != null) {
                redisService.setLastInstruct(textInMessage.getFromUserName(), content);
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

        //原样返回
        if (result == null) {
            result = content;
        }

        //处理字符过长
        result = ByteUtil.limit2048byte(result);

        return MessageUtil.buildTextOutMessage(textInMessage, result);
    }
}
