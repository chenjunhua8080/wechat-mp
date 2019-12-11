package com.cjh.wechatmp.farm;

import com.cjh.wechatmp.api.CloudService;
import com.cjh.wechatmp.dao.BindFarmDao;
import com.cjh.wechatmp.dao.UserDao;
import com.cjh.wechatmp.message.in.TextInMessage;
import com.cjh.wechatmp.po.BindFarmPO;
import com.cjh.wechatmp.po.UserPO;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class FarmService {

    private BindFarmDao bindFarmDao;
    private UserDao userDao;
    private CloudService cloudService;

    public String handleMessage(TextInMessage textInMessage) {
        String result = null;
        if (textInMessage.getContent().equals("绑定农场")) {
            if (isBind(textInMessage.getFromUserName())) {
                result = "已绑定，回复 “hb+openid#xxx” 即可换绑...(xxx -> 农场openid)";
            } else {
                result = "回复 “openid#xxx” 即可绑定...(xxx -> 农场openid)";
            }

        } else if (textInMessage.getContent().contains("hb+openid#")) {
            result = changeBind(textInMessage);

        } else if (textInMessage.getContent().contains("openid#")) {
            result = bind(textInMessage);

        } else if (textInMessage.getContent().equals("今日农场作业情况")) {
            result = getTodayFarmLog(textInMessage.getFromUserName());

        }
        return result;
    }

    /**
     * 今日农场作业情况
     */
    private String getTodayFarmLog(String fromUserName) {
        UserPO userPO = userDao.selectByOpenId(fromUserName);
        if (userPO == null) {
            return "请先授权登录";
        }
        BindFarmPO bindFarmPO = bindFarmDao.selectByUserId(userPO.getId());
        if (bindFarmPO == null) {
            return "未绑定农场，请先回复 “openid#xxx” 进行绑定...(xxx -> 农场openid)";
        }
        List<FarmLogPO> todayFarmLog = cloudService.getTodayFarmLog(bindFarmPO.getFarmOpenid());
        if (todayFarmLog.isEmpty()) {
            return "暂无消息";
        }
        StringBuilder sb = new StringBuilder();
        for (FarmLogPO farmLogPO : todayFarmLog) {
            sb.append(farmLogPO.getMessage()).append("\t");
        }
        return sb.toString();
    }

    /**
     * 换绑
     */
    private String changeBind(TextInMessage textInMessage) {
        String content = textInMessage.getContent();
        String openId = content.substring("hb+openid#".length());
        UserPO userPO = userDao.selectByOpenId(textInMessage.getFromUserName());
        if (userPO == null) {
            return "请先授权登录";
        }
        BindFarmPO bindFarmPO = bindFarmDao.selectByUserId(userPO.getId());
        if (bindFarmPO == null) {
            return "换绑失败，请先回复 “openid#xxx” 进行绑定...(xxx -> 农场openid)";
        } else {
            bindFarmPO.setFarmOpenid(openId);
            bindFarmDao.updateById(bindFarmPO);
            return "换绑成功, 系统会每日自动领取水滴啦~";
        }
    }

    /**
     * 判断是否绑定
     */
    private boolean isBind(String fromUserName) {
        UserPO userPO = userDao.selectByOpenId(fromUserName);
        return userPO != null;
    }

    /**
     * 绑定农场和系统用户
     */
    private String bind(TextInMessage textInMessage) {
        String content = textInMessage.getContent();
        String openId = content.substring("openid#".length());
        UserPO userPO = userDao.selectByOpenId(textInMessage.getFromUserName());
        if (userPO == null) {
            return "请先授权登录";
        }
        BindFarmPO bindFarmPO = bindFarmDao.selectByUserId(userPO.getId());
        if (bindFarmPO == null) {
            return "已绑定农场，请先回复 “hb+openid#xxx” 进行换绑...(xxx -> 农场openid)";
        }
        bindFarmPO = new BindFarmPO();
        bindFarmPO.setFarmOpenid(openId);
        bindFarmPO.setUserId(userPO.getId());
        bindFarmDao.insert(bindFarmPO);
        return "绑定成功, 系统会每日自动领取水滴啦~";
    }

}
