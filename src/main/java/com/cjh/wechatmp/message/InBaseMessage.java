package com.cjh.wechatmp.message;

import lombok.Data;

@Data
public class InBaseMessage {

    private String toUserName;
    private String fromUserName;
    private String createTime;
    private String msgType;
    private String msgId;
}
