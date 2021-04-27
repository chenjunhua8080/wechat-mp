package com.cjh.wechatmp.feign;

import com.alibaba.fastjson.JSONObject;
import com.cjh.wechatmp.avatar.AvatarPO;
import com.cjh.wechatmp.farm.FarmLogPO;
import com.cjh.wechatmp.farm.ReqLog;
import com.cjh.wechatmp.juhe.QuestionBankPO;
import com.cjh.wechatmp.po.NowPlaying;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "common", fallbackFactory = FeignFallBackFactory.class)
public interface CloudFeignClient {

    //########################## 通用请求日志 #############################

    /**
     * 查询请求日志
     */
    @GetMapping("/reqLog/list")
    List<ReqLog> getReqLogList(@RequestParam String openId, @RequestParam Integer platformType, @RequestParam Date date);

    //########################## 头像API #############################
    //不能用int做参数

    /**
     * 头像列表
     */
    @GetMapping("/getAvatarByNew")
    List<AvatarPO> getAvatarByNew(@RequestParam Integer pageNum);

    //########################## 农场API #############################

    /**
     * 今天农场作业情况
     */
    @GetMapping("/getTodayFarmLog")
    List<FarmLogPO> getTodayFarmLog(@RequestParam String openId, @RequestParam Date date);

    //########################## 豆瓣API #############################

    /**
     * 电影列表
     */
    @GetMapping("/getNowPlaying")
    List<NowPlaying> getNowPlaying();

    /**
     * 电影详情
     */
    @GetMapping("/getMovieDesc")
    String getMovieDesc(@RequestParam String id);

    /**
     * 电影评论
     */
    @GetMapping("/getComments")
    List<String> getComments(@RequestParam String id, @RequestParam Integer pageNum, @RequestParam Integer pageSize);

    //########################## 聚合API #############################

    /**
     * 历史上的今天
     */
    @GetMapping("/getTodayHistory")
    String getTodayHistory();

    /**
     * 笑话
     */
    @GetMapping("/getRandJoke")
    String getRandJoke();

    /**
     * 简单天气
     */
    @GetMapping("/getSimpleWeadther")
    String getSimpleWeadther(@RequestParam String city);

    /**
     * 星座运势
     */
    @GetMapping("/getConstellation")
    String getConstellation(@RequestParam String start);

    /**
     * 学车题库
     */
    @GetMapping("/getQuestionBank")
    QuestionBankPO getQuestionBank();

    /**
     * 学车题库-答案
     */
    @GetMapping("/getQuestionAnswers")
    JSONObject getQuestionAnswers();

    //########################## 京东API #############################

    /**
     * 叠蛋糕 - 查询我的金币
     */
    @GetMapping("/getHomeData")
    String getHomeData(@RequestParam String openId);

    /**
     * 叠蛋糕 - 统计领取金币
     */
    @GetMapping("/countCollectScore")
    String countCollectScore(@RequestParam String openId, @RequestParam Date date);

    //########################## 中国银行 #############################

    /**
     * 中国银行 - 查询我的
     */
    @GetMapping("/getBankChinaInfo")
    String getBankChinaInfo(@RequestParam String openId);

    //########################## BOSS #############################

    /**
     * BOSS - 批量下载简历
     * @return map
     */
    @GetMapping("/getResumeZip")
    Map<String, Object> getResumeZip(@RequestParam String openId);
}
