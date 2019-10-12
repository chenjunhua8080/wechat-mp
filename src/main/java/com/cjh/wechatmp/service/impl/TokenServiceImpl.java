package com.cjh.wechatmp.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjh.wechatmp.dao.TokenDao;
import com.cjh.wechatmp.po.TokenPO;
import com.cjh.wechatmp.request.PageRequest;
import com.cjh.wechatmp.request.TokenRequest;
import com.cjh.wechatmp.service.TokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author cjh
 * @email
 * @date 2019-10-11 15:10:33
 */
@Slf4j
@AllArgsConstructor
@Service
public class TokenServiceImpl extends ServiceImpl<TokenDao, TokenPO> implements TokenService {

    @Override
    public IPage<TokenPO> listByPage(TokenRequest tokenRequest, PageRequest pageRequest) {
        Page page = new Page(pageRequest.getPageNum(), pageRequest.getPageSize());
        return baseMapper.listByPage(page, tokenRequest);
    }

}
