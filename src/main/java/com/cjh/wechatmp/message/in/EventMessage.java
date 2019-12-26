package com.cjh.wechatmp.message.in;

import com.cjh.wechatmp.message.BaseMessage;
import lombok.Data;

@Data
public class EventMessage extends BaseMessage {

    /**
     * base
     */
    private String event;
    private String eventKey;
    /**
     * 点击菜单跳转链接时的事件推送
     */
    private String menuID;
}
