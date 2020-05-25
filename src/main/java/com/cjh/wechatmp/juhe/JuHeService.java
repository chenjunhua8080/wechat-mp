package com.cjh.wechatmp.juhe;

import com.cjh.wechatmp.api.CloudService;
import com.cjh.wechatmp.enums.InstructsEnum;
import com.cjh.wechatmp.message.in.TextInMessage;
import com.cjh.wechatmp.redis.RedisService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class JuHeService {

    private CloudService cloudService;
    private RedisService redisService;

    public String handleMessage(TextInMessage textInMessage) {
        String content = textInMessage.getContent();
        String result = null;
        String lastInstruct = redisService.getLastInstruct(textInMessage.getFromUserName(), false);
        if (InstructsEnum.Instruct6.getCode().toString().equals(lastInstruct)) {
            result = cloudService.getConstellation(
                InstructsEnum.getNameByCode(Integer.parseInt(content), InstructsEnum.Instruct6.getCode()));
        } else if (InstructsEnum.Instruct5.getCode().toString().equals(lastInstruct)) {
            result = cloudService.getSimpleWeadther(content);
        } else if (content.contains("天气#")) {
            String city = content.substring(content.indexOf("#") + 1);
            result = cloudService.getSimpleWeadther(city);
        } else if (content.contains("笑话") ||
            InstructsEnum.Instruct7.getCode().toString().equals(content)) {
            result = cloudService.getRandJoke();
        } else if (content.contains("历史上的今天") ||
            InstructsEnum.Instruct8.getCode().toString().equals(content)) {
            result = cloudService.getTodayHistory();
        } else if (content.contains("星座运势#")) {
            String start = content.substring(content.indexOf("#") + 1);
            result = cloudService.getConstellation(start);
        }
        return result;
    }
}
