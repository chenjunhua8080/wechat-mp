package com.cjh.wechatmp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjh.wechatmp.dao.UserDao;
import com.cjh.wechatmp.po.UserPO;
import com.cjh.wechatmp.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author cjh
 * @email
 * @date 2019-10-21 18:44:25
 */
@Slf4j
@AllArgsConstructor
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, UserPO> implements UserService {

    @Override
    public UserPO getByOpenId(String openId) {
        QueryWrapper<UserPO> wrapper = Wrappers.query();
        wrapper.eq("open_id", openId);
        return baseMapper.selectOne(wrapper);
    }

}
