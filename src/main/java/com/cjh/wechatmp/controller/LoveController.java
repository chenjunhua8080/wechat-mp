package com.cjh.wechatmp.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.cjh.wechatmp.redis.RedisService;
import com.cjh.wechatmp.response.Result;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统计时间
 */
@RestController("/mylog")
public class LoveController {

    @Autowired
    private RedisService redisService;

    @GetMapping("/countLoveTime")
    public Result countLoveTime() {
        Date now = new Date();
        String dateStr = redisService.get("date:520");
        if (!StringUtils.isEmpty(dateStr)) {
            DateTime beginDate = DateUtil.parseDateTime(dateStr);
            long y = DateUtil.betweenYear(beginDate, now, true);
            long M = DateUtil.betweenMonth(beginDate, now, true);
            long d = DateUtil.betweenDay(beginDate, now, true);
            long w = DateUtil.betweenWeek(beginDate, now, true);
            long H = DateUtil.between(beginDate, now, DateUnit.HOUR, true);
            long m = DateUtil.between(beginDate, now, DateUnit.MINUTE, true);
            long s = DateUtil.between(beginDate, now, DateUnit.SECOND, true);

            Map<String, Object> data = new HashMap<>(7);
            data.put("y", y);
            data.put("M", M);
            data.put("d", d);
            data.put("w", w);
            data.put("H", H);
            data.put("m", m);
            data.put("s", s);
            data.put("date", dateStr);
            return Result.success().setData(data);
        }
        return Result.success();
    }

}
