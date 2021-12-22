package com.cjh.wechatmp.job;

import com.cjh.wechatmp.util.XxlJobUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TestJob {

    @XxlJob("job.test.test")
    public void test() {
        XxlJobUtil.showLog("[----- test job ----]");
    }

}
