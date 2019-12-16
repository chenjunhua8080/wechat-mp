package com.cjh.wechatmp.message.push;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Temp {

    private String touser;
    private String template_id;
    private String url;
    private MiniprogramBean miniprogram;
    private DataBean data;

    @NoArgsConstructor
    @Data
    public static class MiniprogramBean {

        private String appid;
        private String pagepath;
    }

    @NoArgsConstructor
    @Data
    public static class DataBean {

        private TextBean first;
        private TextBean list;
        private TextBean remark;

        @NoArgsConstructor
        @Data
        public static class TextBean {

            private String value;
            private String color;
        }
    }
}
