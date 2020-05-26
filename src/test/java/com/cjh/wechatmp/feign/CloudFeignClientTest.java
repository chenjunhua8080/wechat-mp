package com.cjh.wechatmp.feign;

import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CloudFeignClientTest {

    @Autowired
    private CloudFeignClient feignClient;

    @Test
    public void getTodayFarmLog() {
        log.info(String.valueOf(feignClient.getTodayFarmLog("o1IsFt8QJMFhVZDE0W3ovHx15hes", new Date())));
    }

    @Test
    public void getTodayHistory() {
        log.info(feignClient.getTodayHistory());
    }

    @Test
    public void getRandJoke() {
        log.info(feignClient.getRandJoke());
    }

    @Test
    public void getSimpleWeadther() {
        log.info(feignClient.getSimpleWeadther("广州"));
    }

    @Test
    public void getConstellation() {
    }

    @Test
    public void getQuestionBank() {
    }

    @Test
    public void getQuestionAnswers() {
    }

    @Test
    public void getAvatarByNew() {
        log.info(String.valueOf(feignClient.getAvatarByNew(3)));
    }
}