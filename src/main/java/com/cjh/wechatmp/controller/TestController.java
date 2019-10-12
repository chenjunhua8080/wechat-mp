package com.cjh.wechatmp.controller;

import com.cjh.wechatmp.po.User;
import com.cjh.wechatmp.redis.RedisService;
import com.cjh.wechatmp.response.Result;
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
