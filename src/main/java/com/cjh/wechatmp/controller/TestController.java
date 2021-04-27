package com.cjh.wechatmp.controller;

import com.cjh.wechatmp.api.CloudService;
import com.cjh.wechatmp.dao.BindFarmDao;
import com.cjh.wechatmp.dao.UserDao;
import com.cjh.wechatmp.enums.PlatformEnum;
import com.cjh.wechatmp.po.BindFarmPO;
import com.cjh.wechatmp.po.User;
import com.cjh.wechatmp.po.UserPO;
import com.cjh.wechatmp.redis.RedisService;
import com.cjh.wechatmp.response.Result;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
//@RefreshScope
@RestController
@RequestMapping("/test")
public class TestController {

//    @Value("${user.name}")
//    private String userName;

    private User user;
    private RedisService redisService;


    private BindFarmDao bindFarmDao;
    private UserDao userDao;
    private CloudService cloudService;

    @GetMapping("/getResumeZip")
    public String getUser(String openId) {
        String result;
        BindFarmPO bind = getBind(openId, PlatformEnum.BOSS_EMAIL.getCode());
        if (bind != null) {
            Map<String, Object> map = cloudService.getResumeZip(openId);
            if (map.get("msg") != null) {
                return map.get("msg").toString();
            }
            result = "已打包" + map.get("count") + "份简历点击<a href='" + map.get("link") + "'>这里</a>下载";
        } else {
            result = "未绑定";
        }
        return result;
    }

    private BindFarmPO getBind(String openId, Integer platformType) {
        UserPO user = userDao.selectByOpenId(openId);
        return bindFarmDao.getBindUser(user.getId(), platformType);
    }


    @GetMapping("/getUser")
    public User getUser() {
        return user;
    }

    @GetMapping("/getUser2")
    public String getUser2() {
        return user.getName();
    }

    @GetMapping("/testRedis/{key}/{val}")
    public Result testRedis(@PathVariable("key") String key, @PathVariable("val") String val) {
        user.setName(val);
        redisService.set(key, user);
        return Result.success().setData(redisService.get(key, User.class));
    }

}
