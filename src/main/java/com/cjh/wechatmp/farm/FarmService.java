package com.cjh.wechatmp.farm;

import com.cjh.wechatmp.api.CloudService;
import com.cjh.wechatmp.boss.BossService;
import com.cjh.wechatmp.dao.BindFarmDao;
import com.cjh.wechatmp.dao.UserDao;
import com.cjh.wechatmp.enums.InstructsEnum;
import com.cjh.wechatmp.enums.PlatformEnum;
import com.cjh.wechatmp.message.in.TextInMessage;
import com.cjh.wechatmp.po.BindFarmPO;
import com.cjh.wechatmp.po.UserPO;
import com.cjh.wechatmp.redis.RedisService;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        String openId = textInMessage.getFromUserName();
        String lastInstruct = redisService.getLastInstruct(openId, false);
        //京东-618蛋糕
        if (InstructsEnum.Instruct2.getCode().toString().equals(lastInstruct)
            && content.equals(InstructsEnum.Instruct21.getCode().toString())) {
            BindFarmPO bind = getBind(openId, PlatformEnum.JD_CAKE.getCode());
            if (bind != null) {
                result = "已绑定: " + bind.getId() + ", 回复cookie==>xxx覆盖绑定";
            } else {
                result = "请回复cookie==>xxx继续完成绑定";
            }
            redisService.setLastInstruct(openId, lastInstruct + "-" + content);

        } else if ((InstructsEnum.Instruct2.getCode() + "-" + InstructsEnum.Instruct21.getCode())
            .equals(lastInstruct)) {
            String cookie = getCookie(textInMessage.getContent());
            if (cookie == null) {
                result = "cookie格式错误，请参考cookie==>xxx";
            } else {
                result = "绑定成功，id: " + doBind(openId, cookie, PlatformEnum.JD_CAKE.getCode());
                //清除指令
                redisService.getLastInstruct(openId, true);
            }

        } else if (InstructsEnum.Instruct2.getCode().toString().equals(lastInstruct)
            && content.equals(InstructsEnum.Instruct22.getCode().toString())) {
            BindFarmPO bind = getBind(openId, PlatformEnum.JD_CAKE.getCode());
            if (bind != null) {
                result = cloudService.getHomeData(openId);
            } else {
                result = "未绑定";
            }
        } else if (InstructsEnum.Instruct2.getCode().toString().equals(lastInstruct)
            && content.equals(InstructsEnum.Instruct23.getCode().toString())) {
            BindFarmPO bind = getBind(openId, PlatformEnum.JD_CAKE.getCode());
            if (bind != null) {
                result = cloudService.countCollectScore(openId, null);
            } else {
                result = "未绑定";
            }
        } else if (InstructsEnum.Instruct2.getCode().toString().equals(lastInstruct)
            && content.equals(InstructsEnum.Instruct24.getCode().toString())) {
            BindFarmPO bind = getBind(openId, PlatformEnum.JD_CAKE.getCode());
            if (bind != null) {
                result = getTodayLog(openId, PlatformEnum.JD_CAKE.getCode(), false);
            } else {
                result = "未绑定";
            }
        }
        //中国银行
        else if (InstructsEnum.Instruct4.getCode().toString().equals(lastInstruct)
            && content.equals(InstructsEnum.Instruct41.getCode().toString())) {
            BindFarmPO bind = getBind(openId, PlatformEnum.BANK_CHINA.getCode());
            if (bind != null) {
                result = "已绑定: " + bind.getId() + ", 回复cookie==>xxx覆盖绑定";
            } else {
                result = "请回复cookie==>xxx继续完成绑定";
            }
            redisService.setLastInstruct(openId, lastInstruct + "-" + content);

        } else if ((InstructsEnum.Instruct4.getCode() + "-" + InstructsEnum.Instruct41.getCode())
            .equals(lastInstruct)) {
            String cookie = getCookie(textInMessage.getContent());
            if (cookie == null) {
                result = "cookie格式错误，请参考cookie==>xxx";
            } else {
                result = "绑定成功，id: " + doBind(openId, cookie, PlatformEnum.BANK_CHINA.getCode());
                //清除指令
                redisService.getLastInstruct(openId, true);
            }
        } else if (InstructsEnum.Instruct4.getCode().toString().equals(lastInstruct)
            && content.equals(InstructsEnum.Instruct42.getCode().toString())) {
            BindFarmPO bind = getBind(openId, PlatformEnum.BANK_CHINA.getCode());
            if (bind != null) {
                result = getTodayLog(openId, PlatformEnum.BANK_CHINA.getCode(), true);
                if (result == null) {
                    result = cloudService.getBankChinaInfo(openId);
                }
            } else {
                result = "未绑定";
            }
        }
        //京东-农场
        else if (content.equals("绑定农场") ||
            (InstructsEnum.Instruct3.getCode().toString().equals(lastInstruct)
                && content.equals(InstructsEnum.Instruct31.getCode().toString()))) {
            BindFarmPO bind = getBind(openId, PlatformEnum.JD_FARM.getCode());
            if (bind != null) {
                result = "已绑定: " + bind.getId() + ", 回复cookie==>xxx覆盖绑定";
            } else {
                result = "请回复cookie==>xxx继续完成绑定";
            }
            redisService.setLastInstruct(openId, lastInstruct + "-" + content);

        } else if (content.contains("openid#") ||
            (InstructsEnum.Instruct3.getCode() + "-" + InstructsEnum.Instruct31.getCode()).equals(lastInstruct)) {
            String cookie = getCookie(textInMessage.getContent());
            if (cookie == null) {
                result = "cookie格式错误，请参考cookie==>xxx";
            } else {
                result = "绑定成功，id: " + doBind(openId, cookie, PlatformEnum.JD_FARM.getCode());
                //清除指令
                redisService.getLastInstruct(openId, true);
            }
        } else if (content.equals("今日农场作业情况") ||
            (InstructsEnum.Instruct3.getCode().toString().equals(lastInstruct)
                && content.equals(InstructsEnum.Instruct32.getCode().toString()))) {
            BindFarmPO bind = getBind(openId, PlatformEnum.JD_FARM.getCode());
            if (bind != null) {
                result = getTodayLog(openId, PlatformEnum.JD_FARM.getCode(), false);
            } else {
                result = "未绑定";
            }

        } else if (InstructsEnum.Instruct3.getCode().toString().equals(lastInstruct)
            && content.equals(InstructsEnum.Instruct33.getCode().toString())) {
            redisService.setLastInstruct(openId, lastInstruct + "-" + content);
            result = "请回复浇水次数";
        } else if (content.contains("连续浇水#") ||
            ((InstructsEnum.Instruct3.getCode() + "-" + InstructsEnum.Instruct33.getCode()).equals(lastInstruct))) {
            int count;
            if (content.contains("#")) {
                count = Integer.parseInt(content.split("#")[1]);
            } else {
                count = Integer.parseInt(content);
            }
            BossService.executorService.execute(() -> cloudService.continuousWater(openId, count));

            result = "正在执行浇水任务，稍后推送结果";
        }
        //京东-宠物
        else if (InstructsEnum.Instruct10.getCode().toString().equals(lastInstruct)
            && InstructsEnum.Instruct101.getCode().toString().equals(content)) {
            BindFarmPO bind = getBind(openId, PlatformEnum.JD_PETS.getCode());
            if (bind != null) {
                result = "已绑定: " + bind.getId() + ", 回复cookie==>xxx覆盖绑定";
            } else {
                result = "请回复cookie==>xxx继续完成绑定";
            }
            redisService.setLastInstruct(openId, lastInstruct + "-" + content);

        } else if ((InstructsEnum.Instruct10.getCode() + "-" + InstructsEnum.Instruct101.getCode())
            .equals(lastInstruct)) {
            String cookie = getCookie(textInMessage.getContent());
            if (cookie == null) {
                result = "cookie格式错误，请参考cookie==>xxx";
            } else {
                result = "绑定成功，id: " + doBind(openId, cookie, PlatformEnum.JD_PETS.getCode());
                //清除指令
                redisService.getLastInstruct(openId, true);
            }
        } else if (InstructsEnum.Instruct10.getCode().toString().equals(lastInstruct)
            && InstructsEnum.Instruct102.getCode().toString().equals(content)) {
            BindFarmPO bind = getBind(openId, PlatformEnum.JD_PETS.getCode());
            if (bind != null) {
                result = getTodayLog(openId, PlatformEnum.JD_PETS.getCode(), false);
            } else {
                result = "未绑定";
            }

        } else if (InstructsEnum.Instruct10.getCode().toString().equals(lastInstruct)
            && content.equals(InstructsEnum.Instruct103.getCode().toString())) {
            redisService.setLastInstruct(openId, lastInstruct + "-" + content);
            result = "请回复喂食次数";
        }else if (content.contains("连续喂食#") ||
            ((InstructsEnum.Instruct10.getCode() + "-" + InstructsEnum.Instruct103.getCode()).equals(lastInstruct))) {
            int count;
            if (content.contains("#")) {
                count = Integer.parseInt(content.split("#")[1]);
            } else {
                count = Integer.parseInt(content);
            }
            BossService.executorService.execute(() -> cloudService.continuousFeed(openId, count));

            result = "正在执行喂食任务，稍后推送结果";
        }

        return result;
    }

    /**
     * 从cookie==>xxx截取xxx
     */
    private String getCookie(String content) {
        Pattern pattern = Pattern.compile("cookie==>(.*)");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
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
