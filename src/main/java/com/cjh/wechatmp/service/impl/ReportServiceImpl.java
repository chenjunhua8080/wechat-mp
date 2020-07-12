package com.cjh.wechatmp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjh.wechatmp.dao.ReportDao;
import com.cjh.wechatmp.po.ReportPO;
import com.cjh.wechatmp.service.ReportService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 那啥业务
 */
@Slf4j
@AllArgsConstructor
@Service
public class ReportServiceImpl extends ServiceImpl<ReportDao, ReportPO> implements ReportService {


    @Override
    public void add(String userId) {
        ReportPO entity = new ReportPO();
        entity.setUserId(userId);
        baseMapper.insert(entity);
    }

    @Override
    public String getReportText(String openId) {
        int countByDay = baseMapper.countByDay(openId);
        int countByWeek = baseMapper.countByWeek(openId);
        int countByMonth = baseMapper.countByMonth(openId);
        int countByUser = baseMapper.countByUser(openId);
        int countByDayMax = baseMapper.countByDayMax(openId);
        int diff = baseMapper.diffByUser(openId);
        StringBuilder sb = new StringBuilder();
        sb.append("〓〓〓〓 今日统计: ").append(countByDay).append(" 〓〓〓〓").append("\n");
        sb.append("〓〓〓〓 本周统计: ").append(countByWeek).append(" 〓〓〓〓").append("\n");
        sb.append("〓〓〓〓 本月统计: ").append(countByMonth).append(" 〓〓〓〓").append("\n");
        sb.append("〓〓〓〓〓〓 总计: ").append(countByUser).append(" 〓〓〓〓");
        sb.append("〓〓 一天最多次数: ").append(countByDayMax).append(" 〓〓〓〓").append("\n");
        sb.append("〓〓 间隔时长最长: ").append(diff).append(" 〓〓〓〓").append("\n");
        return sb.toString();
    }
}
