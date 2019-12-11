package com.cjh.wechatmp.controller;

import com.cjh.wechatmp.po.UserPO;
import com.cjh.wechatmp.request.UserRequest;
import com.cjh.wechatmp.response.Result;
import com.cjh.wechatmp.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cjh
 * @email
 * @date 2019-10-21 18:44:25
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    /**
     * 单个
     */
    @GetMapping("/info")
    public Result info(UserRequest user) {
        UserPO userPO;
        if (user.getOpenId() != null) {
            userPO = userService.getByOpenId(user.getOpenId());
        } else {
            userPO = userService.getById(user.getId());
        }
        return Result.success().setData(userPO);
    }

    /**
     * 绑定
     */
    @PostMapping("/bind")
    public Result list(@RequestBody UserRequest user) {
        UserPO userPO = userService.getByOpenId(user.getOpenId());
        userPO.setPhone(user.getPhone());
        userService.updateById(userPO);
        return Result.success("绑定成功");
    }

}
