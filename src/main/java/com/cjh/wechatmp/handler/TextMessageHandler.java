package com.cjh.wechatmp.handler;

import com.cjh.wechatmp.annotation.MessageProcessor;
import com.cjh.wechatmp.farm.FarmService;
import com.cjh.wechatmp.message.BaseMessage;
import com.cjh.wechatmp.message.MessageUtil;
import com.cjh.wechatmp.message.handler.AbstractMessageHandler;
import com.cjh.wechatmp.message.in.TextInMessage;
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

    @Override
    public BaseMessage doHandle(BaseMessage inMessage) {
        TextInMessage textInMessage = (TextInMessage) inMessage;
        String content;

        //农场业务
        content = farmService.handleMessage(textInMessage);

        //原样返回
        if (content == null) {
            content = textInMessage.getContent();
        }
        return MessageUtil.buildTextOutMessage(textInMessage, content);
    }
}
