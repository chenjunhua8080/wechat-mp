package com.cjh.wechatmp.po.message;

import lombok.Data;

@Data
public class BaseMessage {

    private String toUserName;
    private String FromUserName;
    private String createTime;
    private String msgType;
    private String msgId;
}
