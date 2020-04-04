package com.cjh.wechatmp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjh.wechatmp.po.ReportPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 那啥次数统计
 */
@Mapper
public interface ReportDao extends BaseMapper<ReportPO> {

    /**
     * 根据userID查询
     */
    ReportPO selectByUserId(@Param("userId") String userId);

    /**
     * 统计根据天
     */
    int countByDay(String openId);

    /**
     * 统计根据星期
     */
    int countByWeek(String openId);

    /**
     * 统计根据月
     */
    int countByMonth(String openId);

    /**
     * 统计用户所有
     */
    int countByUser(String openId);
}
