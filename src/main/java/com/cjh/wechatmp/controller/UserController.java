package com.cjh.wechatmp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cjh.wechatmp.po.UserPO;
import com.cjh.wechatmp.request.PageRequest;
import com.cjh.wechatmp.request.UserRequest;
import com.cjh.wechatmp.response.Result;
import com.cjh.wechatmp.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
     * 列表
     */
    @RequestMapping("/list")
    public Result list(UserRequest user, PageRequest pageRequest) {
        IPage<UserPO> pageData = userService.listByPage(user, pageRequest);
        return Result.success().addData(pageData);
    }

    /**
     * 单个
     */
    @RequestMapping("/info")
    public Result info(UserRequest user) {
        UserPO userPO;
        if (user.getOpenId() != null) {
            userPO = userService.getById(user.getOpenId());
        } else {
            userPO = userService.getById(user.getId());
        }
        return Result.success().addData(userPO);
    }

    /**
     * 绑定
     */
    @RequestMapping("/bind")
    public Result list(UserRequest user) {
        UserPO userPO = userService.getByOpenId(user.getOpenId());
        userPO.setPhone(user.getPhone());
        userService.save(userPO);
        return Result.success("绑定成功");
    }

}
