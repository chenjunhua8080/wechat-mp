package com.cjh.wechatmp.po;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "user")
@Component
@Data
public class User {

    private String name;
    private int age;
}
