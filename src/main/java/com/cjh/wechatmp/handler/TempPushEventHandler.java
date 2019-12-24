package com.cjh.wechatmp.handler;

import com.cjh.wechatmp.annotation.MessageProcessor;
import com.cjh.wechatmp.message.BaseMessage;
import com.cjh.wechatmp.message.MessageConstant;
import com.cjh.wechatmp.message.handler.AbstractMessageHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 模板消息推送成功事件消息处理器
 */
@Slf4j
@AllArgsConstructor
@MessageProcessor(messageType = MessageConstant.MESSAGE_TYPE_EVENT, eventType = MessageConstant.EVENT_TYPE_TEMP_PUSH)
@Component
public class TempPushEventHandler extends AbstractMessageHandler {

    @Override
    public BaseMessage doHandle(BaseMessage inMessage) {
        log.info("模板推送消息回调~");
        return null;
    }
}
