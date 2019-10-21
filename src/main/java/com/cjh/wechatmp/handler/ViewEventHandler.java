package com.cjh.wechatmp.handler;

import com.cjh.wechatmp.annotation.MessageProcessor;
import com.cjh.wechatmp.message.BaseMessage;
import com.cjh.wechatmp.message.MessageConstant;
import com.cjh.wechatmp.message.MessageUtil;
import com.cjh.wechatmp.message.handler.AbstractMessageHandler;
import com.cjh.wechatmp.message.in.EventMessage;
import com.cjh.wechatmp.redis.RedisConstant;
import com.cjh.wechatmp.redis.RedisService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * View事件消息处理器
 */
@AllArgsConstructor
@MessageProcessor(messageType = MessageConstant.MESSAGE_TYPE_EVENT, eventType = MessageConstant.EVENT_TYPE_CLICK)
@Component
public class ViewEventHandler extends AbstractMessageHandler {

    private RedisService redisService;

    @Override
    public BaseMessage doHandle(BaseMessage inMessage) {
        EventMessage eventMessage = (EventMessage) inMessage;
        String eventKey = eventMessage.getEventKey();
        String content;

        String userToken = redisService.get(RedisConstant.USER_TOKEN);
        if (userToken == null) {
            content = "未登录哦！<a href=\"" + eventKey + "\">戳我</a>去登陆";
            return MessageUtil.buildTextOutMessage(inMessage, content);
        }

        return null;
    }
}
