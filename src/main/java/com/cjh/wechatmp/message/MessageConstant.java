package com.cjh.wechatmp.message;

public class MessageConstant {

    /**
     * 消息类型
     */
    public static final String MESSAGE_TYPE_TEXT = "text";
    public static final String MESSAGE_TYPE_IMG = "img";
    public static final String MESSAGE_TYPE_IMG_RESP = "image";
    public static final String MESSAGE_TYPE_EVENT = "event";

    /**
     * 事件类型
     */
    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";
    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";
    public static final String EVENT_TYPE_LOCATION = "LOCATION";
    public static final String EVENT_TYPE_CLICK = "CLICK";
    public static final String EVENT_TYPE_VIEW = "VIEW";
    public static final String EVENT_TYPE_TEMP_PUSH = "TEMPLATESENDJOBFINISH";

    /**
     * 进出消息
     */
    public static final int MESSAGE_IN = 1;
    public static final int MESSAGE_OUT = 2;
}
