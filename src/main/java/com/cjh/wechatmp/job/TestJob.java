package com.cjh.wechatmp.job;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@EnableScheduling
@Slf4j
public class TestJob {

    @Scheduled(cron = "${job.test}")
    public void test() {
        log.info("test job: {}", new Date());
    }

}
