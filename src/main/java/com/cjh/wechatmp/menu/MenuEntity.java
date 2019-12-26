package com.cjh.wechatmp.menu;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "mp.menu")
@Component
@NoArgsConstructor
@Data
public class MenuEntity {

    private List<ButtonBean> button;

    @NoArgsConstructor
    @Data
    public static class ButtonBean {

        private String type;
        private String name;
        private String key;
        private List<SubButtonBean> subButton;

        @NoArgsConstructor
        @Data
        public static class SubButtonBean {

            private String type;
            private String name;
            private String url;
            private String appid;
            private String pagepath;
            private String key;
        }
    }
}
