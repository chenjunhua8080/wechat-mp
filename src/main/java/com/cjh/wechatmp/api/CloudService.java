package com.cjh.wechatmp.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cjh.wechatmp.farm.FarmLogPO;
import com.cjh.wechatmp.juhe.QuestionBankPO;
import com.cjh.wechatmp.po.NowPlaying;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class CloudService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 获取热映电影
     */
    public NowPlaying getNowPlaying() {
        String url = CloudApi.HOST_COMMON + "getNowPlaying";
        log.info("服务调用: {}", url);
        NowPlaying nowPlaying = new NowPlaying();
        try {
            String resp = restTemplate.getForObject(url, String.class);
            JSONArray jsonArray = JSONArray.parseArray(resp);
            assert jsonArray != null;
            JSONObject jsonObject = jsonArray.getJSONObject((int) (Math.random() * jsonArray.size()));
            nowPlaying = jsonObject.toJavaObject(NowPlaying.class);
//            nowPlaying.setImg(ImgUtil.img2base64(nowPlaying.getImg()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("服务结果: {}", nowPlaying);
        return nowPlaying;
    }

    /**
     * 获取电影简述
     */
    public String getMovieDesc(String id) {
        String url = CloudApi.HOST_COMMON + "getMovieDesc?id=" + id;
        log.info("服务调用: {}", url);
        String forObject = restTemplate.getForObject(url, String.class);
        log.info("服务结果: {}", forObject);
        return forObject;
    }

    /**
     * 获取电影评论
     */
    public List<String> getComments(String id, int pageNum, int pageSize) {
        String url = CloudApi.HOST_COMMON + "getComments?id=" + id + "&pageNum=" + pageNum + "&pageSize=" + pageSize;
        log.info("服务调用: {}", url);
        List<String> forObject = restTemplate.getForObject(url, List.class);
        log.info("服务结果: {}", forObject);
        return forObject;
    }

    //##################### 农场 ########################

    /**
     * 查询今天农场日志
     */
    public List<FarmLogPO> getTodayFarmLog(String farmOpenId) {
        String url = CloudApi.HOST_COMMON + "getTodayFarmLog?openId=" + farmOpenId;
        log.info("服务调用: {}", url);
        List<Map<String, Object>> forObject = restTemplate.getForObject(url, List.class);
        log.info("服务结果: {}", forObject);
        return JSONObject.parseArray(JSONObject.toJSONString(forObject), FarmLogPO.class);
    }

    //##############################聚合api#################################

    /**
     * 历史上今天
     */
    public String getTodayHistory() {
        String url = CloudApi.HOST_COMMON + "getTodayHistory";
        log.info("服务调用: {}", url);
        String forObject = restTemplate.getForObject(url, String.class);
        log.info("服务结果: {}", forObject);
        return forObject;
    }

    /**
     * 笑话
     */
    public String getRandJoke() {
        String url = CloudApi.HOST_COMMON + "getRandJoke";
        log.info("服务调用: {}", url);
        String forObject = restTemplate.getForObject(url, String.class);
        log.info("服务结果: {}", forObject);
        return forObject;
    }

    /**
     * 天气
     */
    public String getSimpleWeadther(String city) {
        String url = CloudApi.HOST_COMMON + "getSimpleWeadther?city=" + city;
        log.info("服务调用: {}", url);
        String forObject = restTemplate.getForObject(url, String.class);
        log.info("服务结果: {}", forObject);
        return forObject;
    }

    /**
     * 星座
     */
    public String getConstellation(String start) {
        String url = CloudApi.HOST_COMMON + "getConstellation?start=" + start;
        log.info("服务调用: {}", url);
        String forObject = restTemplate.getForObject(url, String.class);
        log.info("服务结果: {}", forObject);
        return forObject;
    }

    /**
     * 学车题库
     */
    public QuestionBankPO getQuestionBank() {
        String url = CloudApi.HOST_COMMON + "getQuestionBank";
        log.info("服务调用: {}", url);
        QuestionBankPO forObject = restTemplate.getForObject(url, QuestionBankPO.class);
        log.info("服务结果: {}", JSONObject.toJSONString(forObject));
        return forObject;
    }

    /**
     * 学车答案
     */
    public JSONObject getQuestionAnswers() {
        String url = CloudApi.HOST_COMMON + "getQuestionAnswers";
        log.info("服务调用: {}", url);
        JSONObject forObject = restTemplate.getForObject(url, JSONObject.class);
        log.info("服务结果: {}", forObject);
        return forObject;
    }
}
