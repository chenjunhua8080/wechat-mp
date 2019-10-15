package com.cjh.wechatmp.message.out;

import com.cjh.wechatmp.message.BaseMessage;
import lombok.Data;

@Data
public class TextOutMessage extends BaseMessage {

    private String content;

}
