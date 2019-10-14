package com.cjh.wechatmp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cjh.wechatmp.po.MessagePO;
import com.cjh.wechatmp.request.MessageRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 消息表
 *
 * @author cjh
 * @email
 * @date 2019-10-14 11:23:26
 */
@Mapper
public interface MessageDao extends BaseMapper<MessagePO> {

    /**
     * 分页查询消息表
     */
    IPage<MessagePO> listByPage(Page page, @Param("params") MessageRequest messageRequest);

    /**
     * 根据msgId查询
     */
    int getByMsgId(@Param("msgId") String msgId);
}
