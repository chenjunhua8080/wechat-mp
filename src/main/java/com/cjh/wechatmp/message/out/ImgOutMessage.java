package com.cjh.wechatmp.message.out;

import com.cjh.wechatmp.message.BaseMessage;
import lombok.Data;

@Data
public class ImgOutMessage extends BaseMessage {

    private Image image;

}
