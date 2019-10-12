package com.cjh.wechatmp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cjh.wechatmp.po.TokenPO;
import com.cjh.wechatmp.request.PageRequest;
import com.cjh.wechatmp.request.TokenRequest;

/**
 * @author cjh
 * @email
 * @date 2019-10-11 15:10:33
 */
public interface TokenService extends IService<TokenPO> {

    /**
     * 分页查询
     */
    IPage<TokenPO> listByPage(TokenRequest tokenRequest, PageRequest pageRequest);

}

