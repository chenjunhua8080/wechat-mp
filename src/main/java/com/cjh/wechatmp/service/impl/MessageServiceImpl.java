package com.cjh.wechatmp.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjh.wechatmp.dao.MessageDao;
import com.cjh.wechatmp.po.MessagePO;
import com.cjh.wechatmp.request.MessageRequest;
import com.cjh.wechatmp.request.PageRequest;
import com.cjh.wechatmp.service.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 消息表
 *
 * @author cjh
 * @email
 * @date 2019-10-14 11:23:26
 */
@Slf4j
@AllArgsConstructor
@Service
public class MessageServiceImpl extends ServiceImpl<MessageDao, MessagePO> implements MessageService {

    @Override
    public IPage<MessagePO> listByPage(MessageRequest messageRequest, PageRequest pageRequest) {
        Page page = new Page(pageRequest.getPageNum(), pageRequest.getPageSize());
        return baseMapper.listByPage(page, messageRequest);
    }

    @Override
    public boolean isExist(String msgId) {
        return baseMapper.getByMsgId(msgId) > 0;
    }

}
