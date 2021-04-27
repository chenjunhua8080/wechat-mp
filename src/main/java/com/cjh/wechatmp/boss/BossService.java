package com.cjh.wechatmp.boss;

import com.cjh.wechatmp.api.CloudService;
import com.cjh.wechatmp.dao.BindFarmDao;
import com.cjh.wechatmp.dao.UserDao;
import com.cjh.wechatmp.enums.InstructsEnum;
import com.cjh.wechatmp.enums.PlatformEnum;
import com.cjh.wechatmp.farm.ReqLog;
import com.cjh.wechatmp.message.in.TextInMessage;
import com.cjh.wechatmp.po.BindFarmPO;
import com.cjh.wechatmp.po.UserPO;
import com.cjh.wechatmp.redis.RedisService;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@AllArgsConstructor
@Component
public class BossService {

    private BindFarmDao bindFarmDao;
    private UserDao userDao;
    private CloudService cloudService;
    private RedisService redisService;

    public String handleMessage(TextInMessage textInMessage) {
        String content = textInMessage.getContent();
        String result = null;
        String openId = textInMessage.getFromUserName();
        String lastInstruct = redisService.getLastInstruct(openId, false);

        //简历批量下载
        if (content.equals("绑定BOSS邮箱") ||
            (InstructsEnum.Instruct11.getCode().toString().equals(lastInstruct)
                && content.equals(InstructsEnum.Instruct111.getCode().toString()))) {
            BindFarmPO bind = getBind(openId, PlatformEnum.BOSS_EMAIL.getCode());
            if (bind != null) {
                result = "已绑定: " + bind.getId() + ", 回复'邮箱号;IMAP码'覆盖绑定";
            } else {
                result = "请回复'邮箱号;IMAP码'继续完成绑定";
            }
            redisService.setLastInstruct(openId, lastInstruct + "-" + content);

        } else if (StringUtils.isEmpty(content.replaceAll("^.*?@.*?;.*$", "")) ||
            (InstructsEnum.Instruct11.getCode() + "-" + InstructsEnum.Instruct111.getCode()).equals(lastInstruct)) {
            String cookie = textInMessage.getContent();
            if (cookie.split(";").length != 2) {
                result = "格式错误，请参考'邮箱号;IMAP码'";
            } else {
                result = "绑定成功，id: " + doBind(openId, cookie, PlatformEnum.BOSS_EMAIL.getCode());
                //清除指令
                redisService.getLastInstruct(openId, true);
            }
        } else if (content.equals("今日简历下载情况") ||
            (InstructsEnum.Instruct11.getCode().toString().equals(lastInstruct)
                && content.equals(InstructsEnum.Instruct112.getCode().toString()))) {
            BindFarmPO bind = getBind(openId, PlatformEnum.BOSS_EMAIL.getCode());
            if (bind != null) {
                result = getTodayLog(openId, PlatformEnum.BOSS_EMAIL.getCode(), false);
            } else {
                result = "未绑定";
            }

        } else if (content.equals(InstructsEnum.Instruct300.getCode().toString())) {
            BindFarmPO bind = getBind(openId, PlatformEnum.BOSS_EMAIL.getCode());
            if (bind != null) {
                Map<String, Object> map = cloudService.getResumeZip(openId);
                if (map.get("msg") != null) {
                    return map.get("msg").toString();
                }
                result = "已打包" + map.get("count") + "份简历点击<a href='" + map.get("link") + "'>这里</a>下载";
            } else {
                result = "未绑定";
            }

        }

        return result;
    }

    private Integer doBind(String openId, String cookie, Integer platformType) {
        UserPO user = userDao.selectByOpenId(openId);
        BindFarmPO old = bindFarmDao.getBindUser(user.getId(), platformType);
        if (old != null) {
            bindFarmDao.deleteById(old.getId());
        }
        BindFarmPO entity = new BindFarmPO();
        entity.setUserId(user.getId());
        entity.setPlatformType(platformType);
        entity.setPlatformId(openId);
        entity.setCookie(cookie);
        bindFarmDao.insert(entity);
        return entity.getId();
    }

    private BindFarmPO getBind(String openId, Integer platformType) {
        UserPO user = userDao.selectByOpenId(openId);
        return bindFarmDao.getBindUser(user.getId(), platformType);
    }

    /**
     * 解析日志，把list转为string
     */
    public String parseLog(List<ReqLog> list, boolean returnNull) {
        if (list == null || list.isEmpty()) {
            return returnNull ? null : "暂无消息";
        }
        StringBuilder sb = new StringBuilder();
        for (ReqLog farmLogPO : list) {
            sb.append(farmLogPO.getMessage()).append("\n");
        }
        if (sb.length() > 0) {
            sb.delete(sb.lastIndexOf("\n"), sb.length());
        }
        return sb.toString();
    }

    /**
     * 今日作业情况
     */
    public String getTodayLog(String openId, int platform, boolean returnNull) {
        List<ReqLog> todayLog = cloudService.getReqLogList(openId, platform, new Date());
        return parseLog(todayLog, returnNull);
    }

}
