package com.cjh.wechatmp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjh.wechatmp.po.BindFarmPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 绑定农场
 *
 * @author chenjunhua
 * @email 1109551489@qq.com
 * @date 2019-12-10 18:00:04
 */
@Mapper
public interface BindFarmDao extends BaseMapper<BindFarmPO> {

    /**
     * 根据userID查询
     */
    BindFarmPO selectByUserId(@Param("userId") Integer userId);
}
