package com.cjh.wechatmp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cjh.wechatmp.po.TokenPO;
import com.cjh.wechatmp.request.TokenRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * 
 * @author cjh
 * @email 
 * @date 2019-10-11 15:10:33
 */
@Mapper
public interface TokenDao extends BaseMapper<TokenPO> {

    /**
     * 分页查询
     */
    IPage<TokenPO> listByPage(Page page, @Param("params") TokenRequest tokenRequest);
	
}
