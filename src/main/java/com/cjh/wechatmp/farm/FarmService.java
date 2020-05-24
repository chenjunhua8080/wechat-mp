package com.cjh.wechatmp.farm;

import com.cjh.wechatmp.api.CloudService;
import com.cjh.wechatmp.dao.BindFarmDao;
import com.cjh.wechatmp.dao.UserDao;
import com.cjh.wechatmp.enums.InstructsEnum;
import com.cjh.wechatmp.enums.PlatformEnum;
import com.cjh.wechatmp.message.in.TextInMessage;
import com.cjh.wechatmp.po.BindFarmPO;
import com.cjh.wechatmp.po.UserPO;
import com.cjh.wechatmp.redis.RedisService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class FarmService {

    private BindFarmDao bindFarmDao;
    private UserDao userDao;
    private CloudService cloudService;
    private RedisService redisService;

    public String handleMessage(TextInMessage textInMessage) {
        String content = textInMessage.getContent();
        String result = null;
        String fromUserName = textInMessage.getFromUserName();
        String lastInstruct = redisService.getLastInstruct(fromUserName, true);
        //京东-618蛋糕
        if (InstructsEnum.Instruct2.getCode().toString().equals(lastInstruct)
            && content.equals(InstructsEnum.Instruct21.getCode().toString())) {
            BindFarmPO bind = getBind(fromUserName, PlatformEnum.JD_CAKE.getCode());
            if (bind != null) {
                result = "已绑定: " + bind.getId() + ", 回复cookie覆盖绑定";
            } else {
                result = "请回复完整cookie继续完成绑定";
            }
            redisService.setLastInstruct(fromUserName, lastInstruct + "-" + content);

        } else if ((InstructsEnum.Instruct2.getCode() + "-" + InstructsEnum.Instruct21.getCode())
            .equals(lastInstruct)) {
            result = "绑定成功，id: " + doBind(fromUserName, textInMessage.getContent(), PlatformEnum.JD_CAKE.getCode());

        } else if (InstructsEnum.Instruct2.getCode().toString().equals(lastInstruct)
            && content.equals(InstructsEnum.Instruct22.getCode().toString())) {
            BindFarmPO bind = getBind(fromUserName, PlatformEnum.JD_CAKE.getCode());
            if (bind != null) {
                result = cloudService.getHomeData(fromUserName);
            } else {
                result = "未绑定";
            }
        } else if (InstructsEnum.Instruct2.getCode().toString().equals(lastInstruct)
            && content.equals(InstructsEnum.Instruct23.getCode().toString())) {
            BindFarmPO bind = getBind(fromUserName, PlatformEnum.JD_CAKE.getCode());
            if (bind != null) {
                result = cloudService.countCollectScore(fromUserName, null);
            } else {
                result = "未绑定";
            }
        }
        //中国银行
        else if (InstructsEnum.Instruct4.getCode().toString().equals(lastInstruct)
            && content.equals(InstructsEnum.Instruct41.getCode().toString())) {
            BindFarmPO bind = getBind(fromUserName, PlatformEnum.BANK_CHINA.getCode());
            if (bind != null) {
                result = "已绑定: " + bind.getId() + ", 回复cookie覆盖绑定";
            } else {
                result = "请回复完整cookie继续完成绑定";
            }
            redisService.setLastInstruct(fromUserName, lastInstruct + "-" + content);

        } else if ((InstructsEnum.Instruct4.getCode() + "-" + InstructsEnum.Instruct41.getCode())
            .equals(lastInstruct)) {
            result = "绑定成功，id: " + doBind(fromUserName, textInMessage.getContent(), PlatformEnum.BANK_CHINA.getCode());

        } else if (InstructsEnum.Instruct4.getCode().toString().equals(lastInstruct)
            && content.equals(InstructsEnum.Instruct42.getCode().toString())) {
            BindFarmPO bind = getBind(fromUserName, PlatformEnum.BANK_CHINA.getCode());
            if (bind != null) {
                result = cloudService.getHomeData(fromUserName);
            } else {
                result = "未绑定";
            }
        }
        //京东-农场
        else if (content.equals("绑定农场") ||
            (InstructsEnum.Instruct3.getCode().toString().equals(lastInstruct)
                && content.equals(InstructsEnum.Instruct31.getCode().toString()))) {
            BindFarmPO bind = getBind(fromUserName, PlatformEnum.JD_FARM.getCode());
            if (bind != null) {
                result = "已绑定: " + bind.getId() + ", 回复cookie覆盖绑定";
            } else {
                result = "请回复完整cookie继续完成绑定";
            }
            redisService.setLastInstruct(fromUserName, lastInstruct + "-" + content);

        } else if (content.contains("openid#") ||
            (InstructsEnum.Instruct3.getCode() + "-" + InstructsEnum.Instruct31.getCode()).equals(lastInstruct)) {
            result = "绑定成功，id: " + doBind(fromUserName, textInMessage.getContent(), PlatformEnum.JD_FARM.getCode());

        } else if (content.equals("今日农场作业情况") ||
            (InstructsEnum.Instruct3.getCode().toString().equals(lastInstruct)
                && content.equals(InstructsEnum.Instruct32.getCode().toString()))) {
            BindFarmPO bind = getBind(fromUserName, PlatformEnum.JD_FARM.getCode());
            if (bind != null) {
                result = getTodayFarmLog(fromUserName);
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
     * 今日农场作业情况
     */
    public String getTodayFarmLog(String openId) {
        UserPO userPO = userDao.selectByOpenId(openId);
        if (userPO == null) {
            return "请先授权登录";
        }
        BindFarmPO bindFarmPO = bindFarmDao.selectByUserId(userPO.getId());
        if (bindFarmPO == null) {
            return "未绑定农场，请先回复 “openid#xxx” 进行绑定...(xxx -> 农场openid)";
        }
        List<FarmLogPO> todayFarmLog = cloudService.getTodayFarmLog(bindFarmPO.getFarmOpenid());
        if (todayFarmLog == null || todayFarmLog.isEmpty()) {
            return "暂无消息";
        }
        StringBuilder sb = new StringBuilder();
        for (FarmLogPO farmLogPO : todayFarmLog) {
            sb.append(farmLogPO.getMessage()).append("\n");
        }
        if (sb.length() > 0) {
            sb.delete(sb.lastIndexOf("\n"), sb.length());
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
        BindFarmPO bindFarmPO = bindFarmDao.selectByUserId(userPO.getId());
        return bindFarmPO != null;
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

    /**
     * 今日中银作业情况
     */
    public String getBankChinaLog(String userId) {
        List<FarmLogPO> todayFarmLog = cloudService.getTodayFarmLog(userId);
        if (todayFarmLog == null || todayFarmLog.isEmpty()) {
            return "暂无消息";
        }
        StringBuilder sb = new StringBuilder();
        for (FarmLogPO farmLogPO : todayFarmLog) {
            sb.append(farmLogPO.getMessage()).append("\n");
        }
        if (sb.length() > 0) {
            sb.delete(sb.lastIndexOf("\n"), sb.length());
        }
        return sb.toString();
    }

}
