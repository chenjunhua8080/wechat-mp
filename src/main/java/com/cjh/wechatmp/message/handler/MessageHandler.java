package com.cjh.wechatmp.message.handler;

import com.cjh.wechatmp.message.BaseMessage;

/**
 * 消息处理器接口
 */
public interface MessageHandler {

    /**
     * 真正的处理消息的方法
     */
    BaseMessage doHandle(BaseMessage inMessage);
}
