package com.cjh.wechatmp.po.message.in;

import com.cjh.wechatmp.po.message.BaseMessage;
import lombok.Data;

@Data
public class ImgMessage extends BaseMessage {

    private String PicUrl;
    private String MediaId;

}
