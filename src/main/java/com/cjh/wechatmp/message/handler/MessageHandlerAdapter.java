package com.cjh.wechatmp.message.handler;

import com.cjh.wechatmp.annotation.MessageProcessor;

/**
 * 消息处理器适配器，根据消息类型找到对应处理器<br/>
 *
 * @see MessageProcessor 消息处理器注解<br/>
 */
public interface MessageHandlerAdapter {

    /**
     * 将不同类型的消息发送给对应的消息处理器
     *
     * @param messageType 用户发送给公众号的消息类型
     * @param eventType   事件类型
     * @return 对应的消息处理器
     */
    AbstractMessageHandler findMessageHandler(String messageType, String eventType);

}
