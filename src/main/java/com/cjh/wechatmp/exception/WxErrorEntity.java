package com.cjh.wechatmp.exception;


import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class WxErrorEntity {

    private int errcode;
    private String errmsg;
    private String msg_id;
}
