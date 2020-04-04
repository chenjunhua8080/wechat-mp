package com.cjh.wechatmp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjh.wechatmp.po.ReportPO;

/**
 * 那啥业务
 */
public interface ReportService extends IService<ReportPO> {

    /**
     * 加一次
     */
    void add(String userId);

    /**
     * 获取统计文本模板
     */
    String getReportText(String openId);

}

