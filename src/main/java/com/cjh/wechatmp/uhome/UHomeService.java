package com.cjh.wechatmp.uhome;

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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UHomeService {

    private BindFarmDao bindFarmDao;
    private UserDao userDao;
    private CloudService cloudService;
    private RedisService redisService;

    public String handleMessage(TextInMessage textInMessage) {
        String content = textInMessage.getContent();
        String result = null;
        String openId = textInMessage.getFromUserName();
        String lastInstruct = redisService.getLastInstruct(openId, false);

        if (InstructsEnum.Instruct13.getCode().toString().equals(lastInstruct)
            && content.equals(InstructsEnum.Instruct131.getCode().toString())) {
            //绑定1
            BindFarmPO bind = getBind(openId, PlatformEnum.UHOME.getCode());
            if (bind != null) {
                result = "已绑定: " + bind.getId() + ", 回复'memberId;city;phone;x-auth-token'覆盖绑定";
            } else {
                result = "请回复'memberId;city;phone;x-auth-token'继续完成绑定";
            }
            redisService.setLastInstruct(openId, lastInstruct + "-" + content);
        } else if ((InstructsEnum.Instruct13.getCode() + "-" + InstructsEnum.Instruct131.getCode())
            .equals(lastInstruct)) {
            //绑定2
            String cookie = textInMessage.getContent();
            if (cookie.split(";").length != 4) {
                result = "格式错误，请参考'memberId;city;phone;x-auth-token'";
            } else {
                result = "绑定成功，id: " + doBind(openId, cookie, PlatformEnum.UHOME.getCode());
                //清除指令
                redisService.getLastInstruct(openId, true);
            }
        } else if ((InstructsEnum.Instruct13.getCode().toString().equals(lastInstruct)
            && content.equals(InstructsEnum.Instruct132.getCode().toString()))) {
            //日志
            BindFarmPO bind = getBind(openId, PlatformEnum.UHOME.getCode());
            if (bind != null) {
                result = getTodayLog(openId, PlatformEnum.UHOME.getCode(), false);
            } else {
                result = "未绑定";
            }
        } else if ((InstructsEnum.Instruct13.getCode().toString().equals(lastInstruct)
            && content.equals(InstructsEnum.Instruct133.getCode().toString()))) {
            //签到
            BindFarmPO bind = getBind(openId, PlatformEnum.UHOME.getCode());
            if (bind != null) {
                new Thread(() -> cloudService.uHomeSign(openId)).start();
                result = "指令执行成功";
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
