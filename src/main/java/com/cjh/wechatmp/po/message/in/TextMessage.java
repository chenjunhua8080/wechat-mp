package com.cjh.wechatmp.po.message.in;

import com.cjh.wechatmp.po.message.BaseMessage;
import lombok.Data;

@Data
public class TextMessage extends BaseMessage {

    private String content;

}
