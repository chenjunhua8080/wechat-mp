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

        private TextBean text1;
        private TextBean text2;
        private TextBean text3;
        private TextBean text4;
        private TextBean text5;
        private TextBean text6;

        @NoArgsConstructor
        @Data
        public static class TextBean {

            private String value;
            private String color;
        }
    }
}
