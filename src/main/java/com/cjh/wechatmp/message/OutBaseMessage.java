package com.cjh.wechatmp.message;

import lombok.Data;

@Data
public class OutBaseMessage {

    private String toUserName;
    private String fromUserName;
    private String createTime;
    private String msgType;

}
