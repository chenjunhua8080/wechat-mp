package com.cjh.wechatmp.juhe;

import com.cjh.wechatmp.api.CloudService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class JuHeService {

    private CloudService cloudService;

    public String handleMessage(String content) {
        String result = null;
        if (content.contains("天气#")) {
            String city = content.substring(content.indexOf("#") + 1);
            result = cloudService.getSimpleWeadther(city);
        } else if (content.contains("笑话")) {
            result = cloudService.getRandJoke();
        } else if (content.contains("历史上的今天")) {
            result = cloudService.getTodayHistory();
        } else if (content.contains("星座运势#")) {
            String start = content.substring(content.indexOf("#") + 1);
            result = cloudService.getConstellation(start);
        }
        return result;
    }
}
