package com.cjh.wechatmp.message.in;

import com.cjh.wechatmp.message.BaseMessage;
import lombok.Data;

@Data
public class TextInMessage extends BaseMessage {

    private String content;

}
