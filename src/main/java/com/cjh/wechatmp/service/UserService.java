package com.cjh.wechatmp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cjh.wechatmp.po.UserPO;
import com.cjh.wechatmp.request.PageRequest;
import com.cjh.wechatmp.request.UserRequest;

/**
 * @author cjh
 * @email
 * @date 2019-10-21 18:44:25
 */
public interface UserService extends IService<UserPO> {

    /**
     * 分页查询
     */
    IPage<UserPO> listByPage(UserRequest userRequest, PageRequest pageRequest);

    UserPO getByOpenId(String openId);
}

