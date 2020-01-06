package com.cjh.wechatmp.media;


import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MediaEntity {

    private int errcode;
    private String errmsg;
    private String type;
    private String media_id;
    private String created_at;
}
