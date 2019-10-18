package com.cjh.wechatmp.annotation;

import com.cjh.wechatmp.message.MessageConstant;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 消息处理器注解
 * <p>
 * <strong>messageType</strong> 消息类型(default:text)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageProcessor {

    /**
     * 标识要处理的消息类型
     */
    String messageType() default MessageConstant.MESSAGE_TYPE_TEXT;

    /**
     * 标识要处理的事件类型，当messageType=event时有效
     */
    String eventType() default MessageConstant.EVENT_TYPE_CLICK;

}
