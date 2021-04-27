package com.cjh.wechatmp.message.push;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 模板消息配置
 */
@RefreshScope
@Component
@ConfigurationProperties(prefix = "mp.temp")
@Data
public class TemplateConfig {

    private String farm;
    private String report;
    private String job;
    private String error;
    private String resume;

}
