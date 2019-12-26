package com.cjh.wechatmp.message.in;

import com.cjh.wechatmp.message.BaseMessage;
import lombok.Data;

@Data
public class ImgInMessage extends BaseMessage {

    private String picUrl;
    private String mediaId;

}
