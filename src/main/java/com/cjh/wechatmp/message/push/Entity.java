package com.cjh.wechatmp.message.push;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Entity {

    private String msgtype;
    private TextBean text;
    private List<String> touser;

    @NoArgsConstructor
    @Data
    public static class TextBean {

        private String content;
    }
}
