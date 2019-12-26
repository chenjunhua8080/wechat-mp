package com.cjh.wechatmp.message;

import lombok.Data;

@Data
public class BaseMessage {

    private String toUserName;
    private String fromUserName;
    private Long createTime;
    private String msgType;
    private String msgId;
}
