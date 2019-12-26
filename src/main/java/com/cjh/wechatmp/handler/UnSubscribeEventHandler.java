package com.cjh.wechatmp.handler;

import com.cjh.wechatmp.annotation.MessageProcessor;
import com.cjh.wechatmp.message.BaseMessage;
import com.cjh.wechatmp.message.MessageConstant;
import com.cjh.wechatmp.message.MessageUtil;
import com.cjh.wechatmp.message.handler.AbstractMessageHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 取消关注事件消息处理器
 */
@AllArgsConstructor
@MessageProcessor(messageType = MessageConstant.MESSAGE_TYPE_EVENT, eventType = MessageConstant.EVENT_TYPE_UNSUBSCRIBE)
@Component
public class UnSubscribeEventHandler extends AbstractMessageHandler {

    @Override
    public BaseMessage doHandle(BaseMessage inMessage) {
        String content;
        content = "江湖再见T^T";
        return MessageUtil.buildTextOutMessage(inMessage, content);
    }
}
