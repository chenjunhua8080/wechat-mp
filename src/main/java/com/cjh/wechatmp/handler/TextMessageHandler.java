package com.cjh.wechatmp.handler;

import com.cjh.wechatmp.annotation.MessageProcessor;
import com.cjh.wechatmp.message.BaseMessage;
import com.cjh.wechatmp.message.MessageUtil;
import com.cjh.wechatmp.message.handler.AbstractMessageHandler;
import com.cjh.wechatmp.message.in.TextInMessage;
import org.springframework.stereotype.Component;

/**
 * 文本消息处理器
 */
@MessageProcessor
@Component
public class TextMessageHandler extends AbstractMessageHandler {

    @Override
    public BaseMessage doHandle(BaseMessage inMessage) {
        TextInMessage textInMessage = (TextInMessage) inMessage;
        return MessageUtil.buildTextOutMessage(textInMessage, textInMessage.getContent());
    }
}
