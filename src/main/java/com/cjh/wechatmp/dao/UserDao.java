package com.cjh.wechatmp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjh.wechatmp.po.UserPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cjh
 * @email
 * @date 2019-10-21 18:44:25
 */
@Mapper
public interface UserDao extends BaseMapper<UserPO> {

    /**
     * 根据openId查找
     */
    UserPO selectByOpenId(String openId);
}
