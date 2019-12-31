package com.cjh.wechatmp.handler;

import com.cjh.wechatmp.annotation.MessageProcessor;
import com.cjh.wechatmp.message.BaseMessage;
import com.cjh.wechatmp.message.MessageConstant;
import com.cjh.wechatmp.message.MessageUtil;
import com.cjh.wechatmp.message.handler.AbstractMessageHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 关注事件消息处理器
 */
@AllArgsConstructor
@MessageProcessor(messageType = MessageConstant.MESSAGE_TYPE_EVENT, eventType = MessageConstant.EVENT_TYPE_SUBSCRIBE)
@Component
public class SubscribeEventHandler extends AbstractMessageHandler {

    @Override
    public BaseMessage doHandle(BaseMessage inMessage) {
        String content;
        content = "嗨, 终于等到你了~\t"
            + "<a href=\"http://h5.springeasy.cn/pages/user/user\">戳我</a>绑定手机号";
        return MessageUtil.buildTextOutMessage(inMessage, content);
    }
}
