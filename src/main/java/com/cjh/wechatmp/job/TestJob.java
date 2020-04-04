package com.cjh.wechatmp.job;

import com.cjh.wechatmp.po.UserPO;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@AllArgsConstructor
@EnableScheduling
@Slf4j
@RefreshScope
public class TestJob {

    @Scheduled(cron = "${job.test}")
    public void test() {
        log.info("test job: {}", new Date());
    }

}
