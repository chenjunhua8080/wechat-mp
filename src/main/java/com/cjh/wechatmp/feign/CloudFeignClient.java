package com.cjh.wechatmp.feign;

import com.alibaba.fastjson.JSONObject;
import com.cjh.wechatmp.avatar.AvatarPO;
import com.cjh.wechatmp.farm.FarmLogPO;
import com.cjh.wechatmp.juhe.QuestionBankPO;
import com.cjh.wechatmp.po.NowPlaying;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "common", fallbackFactory = FeignFallBackFactory.class)
public interface CloudFeignClient {

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
    List<FarmLogPO> getTodayFarmLog(@RequestParam String openId);

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

}