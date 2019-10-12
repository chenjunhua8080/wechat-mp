package com.cjh.wechatmp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cjh.wechatmp.po.TokenPO;
import com.cjh.wechatmp.request.PageRequest;
import com.cjh.wechatmp.request.TokenRequest;
import com.cjh.wechatmp.response.Result;
import com.cjh.wechatmp.service.TokenService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cjh
 * @email
 * @date 2019-10-11 15:10:33
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/token")
public class TokenController {

    private TokenService tokenService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result list(TokenRequest token, PageRequest pageRequest) {
        IPage<TokenPO> pageData = tokenService.listByPage(token, pageRequest);
        return Result.success().addData(pageData);
    }


    /**
     * 详情
     */
    @GetMapping("/info")
    public Result info(@RequestParam("id") Integer id) {
        TokenPO token = tokenService.getById(id);
        return Result.success().addData(token);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public Result save(TokenRequest token) {
        TokenPO tokenPO = new TokenPO();
        BeanUtils.copyProperties(token, tokenPO);
        tokenService.save(tokenPO);
        return Result.success();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public Result update(TokenRequest token) {
        TokenPO tokenPO = new TokenPO();
        BeanUtils.copyProperties(token, tokenPO);
        tokenService.updateById(tokenPO);
        return Result.success();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public Result delete(@RequestParam List<Integer> ids) {
        tokenService.removeByIds(ids);
        return Result.success();
    }

}
