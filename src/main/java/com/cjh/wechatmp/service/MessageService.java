package com.cjh.wechatmp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cjh.wechatmp.po.MessagePO;
import com.cjh.wechatmp.request.MessageRequest;
import com.cjh.wechatmp.request.PageRequest;

/**
 * 消息表
 *
 * @author cjh
 * @email
 * @date 2019-10-14 11:23:26
 */
public interface MessageService extends IService<MessagePO> {

    /**
     * 分页查询消息表
     */
    IPage<MessagePO> listByPage(MessageRequest messageRequest, PageRequest pageRequest);

    boolean isExist(String msgId);
}

