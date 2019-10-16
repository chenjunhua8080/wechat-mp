package com.cjh.wechatmp.message.handler;

import com.alibaba.fastjson.JSONObject;
import com.cjh.wechatmp.message.BaseMessage;
import com.cjh.wechatmp.message.MessageConstant;
import com.cjh.wechatmp.po.MessagePO;
import com.cjh.wechatmp.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 消息处理器的抽象类，可继承该类实现在处理消息时执行一些额外工作，例如消息过滤 <br/>
 */
@Slf4j
public abstract class AbstractMessageHandler implements MessageHandler {

    /**
     * 接收各类请求消息，并返回消息
     */
    public final BaseMessage handleMessage(BaseMessage inMessage) {
        BaseMessage outMessage = null;
        try {
            if (accept(inMessage)) {
                preHandle(inMessage);
                outMessage = doHandle(inMessage);
            } else {
                log.info("前置检查不通过! {}", inMessage);
            }
        } catch (Exception e) {
            onError(inMessage, e);
        } finally {
            if (outMessage != null) {
                postHandle(inMessage, outMessage);
            }
        }
        return outMessage;

    }

    /**
     * 消息处理器处理消息的前置检查条件，
     *
     * @param inMessage 请求的消息对象
     * @return 当该方法返回true时才会真正调用处理消息的方法，当方法返回false时，
     *     不执行真正处理消息的方法，也不会执行postHandleMessage方法，会直接返回null
     */
    @Autowired
    private MessageService messageService;

    protected boolean accept(BaseMessage inMessage) {
        log.info("前置检查: {}", inMessage);
        //避免重复处理
        boolean exist = messageService.isExist(inMessage.getMsgId());
        if (exist) {
            log.info("已处理: msgId= {}", inMessage.getMsgId());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

    /**
     * 保存消息
     */
    private void save(BaseMessage baseMessage, int inOrOut) {
        MessagePO inMessagePO = new MessagePO();
        inMessagePO.setInOut(inOrOut);
        inMessagePO.setMsgId(baseMessage.getMsgId());
        inMessagePO.setType(baseMessage.getMsgType());
        inMessagePO.setBody(JSONObject.toJSONString(baseMessage));
        messageService.save(inMessagePO);
    }

    /**
     * 可重写该方法实现在处理消息前对消息进行处理
     */
    protected void preHandle(BaseMessage inMessage) {
        log.info("处理消息前操作: {}", inMessage);
        save(inMessage, MessageConstant.MESSAGE_IN);
    }

    /**
     * 可重写该方法实现在处理消息后对消息进行处理
     */
    protected void postHandle(BaseMessage inMessage, BaseMessage outMessage) {
        log.info("处理消息后操作: {}", outMessage);
        save(outMessage, MessageConstant.MESSAGE_OUT);
    }

    /**
     * 当处理消息的过程中出错时，将执行该方法（默认使用日志记录错误信息到控制台），可重写该方法实现自定义业务
     */
    protected void onError(BaseMessage inMessage, Exception e) {
        log.error("当处理消息的过程中出错时: {} inMessage: {} ", e.getMessage(), inMessage);
        e.printStackTrace();
    }

}
