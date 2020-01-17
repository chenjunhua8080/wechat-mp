package com.cjh.wechatmp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.cjh.wechatmp.dao")
public class WechatMpApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatMpApplication.class, args);
    }

}
