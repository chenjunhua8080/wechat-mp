package com.cjh.wechatmp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cjh.wechatmp.po.UserPO;
import com.cjh.wechatmp.request.UserRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author cjh
 * @email
 * @date 2019-10-21 18:44:25
 */
@Mapper
public interface UserDao extends BaseMapper<UserPO> {

    /**
     * 分页查询
     */
    IPage<UserPO> listByPage(Page page, @Param("params") UserRequest userRequest);

}
