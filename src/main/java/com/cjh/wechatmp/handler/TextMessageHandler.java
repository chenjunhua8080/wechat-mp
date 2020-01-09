package com.cjh.wechatmp.handler;

import com.cjh.wechatmp.annotation.MessageProcessor;
import com.cjh.wechatmp.avatar.AvatarService;
import com.cjh.wechatmp.farm.FarmService;
import com.cjh.wechatmp.juhe.JuHeService;
import com.cjh.wechatmp.message.BaseMessage;
import com.cjh.wechatmp.message.MessageUtil;
import com.cjh.wechatmp.message.handler.AbstractMessageHandler;
import com.cjh.wechatmp.message.in.TextInMessage;
import com.cjh.wechatmp.util.ByteUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 文本消息处理器
 */
@MessageProcessor
@AllArgsConstructor
@Component
public class TextMessageHandler extends AbstractMessageHandler {

    private FarmService farmService;
    private JuHeService juHeService;
    private AvatarService avatarService;

    private static String[] instructs = new String[]{
        "绑定农场",
        "今日农场作业情况",
        "天气#广州",
        "星座运势#处女座",
        "笑话",
        "历史上的今天",
        "头像",
        "help"};

    @Override
    public BaseMessage doHandle(BaseMessage inMessage) {
        TextInMessage textInMessage = (TextInMessage) inMessage;
        String content = textInMessage.getContent();
        String result = null;

        //原样返回
        if ("help".equals(content)) {
            result = "";
            for (int i = 0; i < instructs.length; i++) {
                result += (i + 1) + "、" + instructs[i] + "\n";
            }
        }

        //农场业务
        if (result == null) {
            result = farmService.handleMessage(textInMessage);
        }

        //聚合api业务
        if (result == null) {
            result = juHeService.handleMessage(content);
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
