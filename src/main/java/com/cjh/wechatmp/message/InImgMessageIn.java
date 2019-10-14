package com.cjh.wechatmp.message;

import lombok.Data;

@Data
public class InImgMessageIn extends InBaseMessage {

    private String picUrl;
    private String mediaId;

}
