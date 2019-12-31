package com.cjh.wechatmp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjh.wechatmp.po.UserPO;

/**
 * @author cjh
 * @email
 * @date 2019-10-21 18:44:25
 */
public interface UserService extends IService<UserPO> {

    UserPO getByOpenId(String openId);
}

