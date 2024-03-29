package com.cjh.wechatmp.api;

import com.alibaba.fastjson.JSONObject;
import com.cjh.wechatmp.avatar.AvatarPO;
import com.cjh.wechatmp.farm.FarmLogPO;
import com.cjh.wechatmp.farm.ReqLog;
import com.cjh.wechatmp.feign.CloudFeignClient;
import com.cjh.wechatmp.juhe.QuestionBankPO;
import com.cjh.wechatmp.media.MediaService;
import com.cjh.wechatmp.po.NowPlaying;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Component
@AllArgsConstructor
public class CloudService {

    private CloudFeignClient feignClient;
    private MediaService mediaService;

    /**
     * 获取热映电影-list
     */
    public List<NowPlaying> getNowPlayingList() {
        return feignClient.getNowPlaying();
    }

    /**
     * 获取热映电影-单个
     */
    public NowPlaying getNowPlaying() {
        List<NowPlaying> nowPlayingList = getNowPlayingList();
        if (nowPlayingList == null || nowPlayingList.isEmpty()) {
            return null;
        }
        return nowPlayingList.get((int) (nowPlayingList.size() * Math.random()));
    }

    /**
     * 获取电影简述
     */
    public String getMovieDesc(String id) {
        return feignClient.getMovieDesc(id);
    }

    /**
     * 获取电影评论
     */
    public List<String> getComments(String id, int pageNum, int pageSize) {
        return feignClient.getComments(id, pageNum, pageSize);
    }

    //##################### 农场 ########################

    /**
     * 查询今天农场日志
     */
    public List<FarmLogPO> getTodayFarmLog(String farmOpenId) {
        return feignClient.getTodayFarmLog(farmOpenId, new Date());
    }

    /**
     * 连续浇水
     */
    public String continuousWater(@RequestParam String openId, @RequestParam Integer count) {
        return feignClient.continuousWater(openId, count);
    }

    /**
     * 连续喂食
     */
    public String continuousFeed(@RequestParam String openId, @RequestParam Integer count) {
        return feignClient.continuousFeed(openId, count);
    }

    //##############################聚合api#################################

    /**
     * 历史上今天
     */
    public String getTodayHistory() {
        return feignClient.getTodayHistory();
    }

    /**
     * 笑话
     */
    public String getRandJoke() {
        return feignClient.getRandJoke();
    }

    /**
     * 天气
     */
    public String getSimpleWeadther(String city) {
        return feignClient.getSimpleWeadther(city);
    }

    /**
     * 星座
     */
    public String getConstellation(String start) {
        return feignClient.getConstellation(start);
    }

    /**
     * 学车题库
     */
    public QuestionBankPO getQuestionBank() {
        return feignClient.getQuestionBank();
    }

    /**
     * 学车答案
     */
    public JSONObject getQuestionAnswers() {
        return feignClient.getQuestionAnswers();
    }

    /**
     * 头像列表
     */
    public List<AvatarPO> getAvatarByNew(int pageNum) {
        return feignClient.getAvatarByNew(pageNum);
    }

    /**
     * 头像图片
     */
    public String getAvatar() {
        //获取图片链接
        List<AvatarPO> list = getAvatarByNew((int) (Math.random() * 10));
        if (list == null || list.isEmpty()) {
            return null;
        }

        AvatarPO avatarPO = list.get((int) (Math.random() * list.size()));
        String url = avatarPO.getImg();
        //下载图片
        File file = new File("avatar_" + System.currentTimeMillis() + url.substring(url.lastIndexOf(".")));
        downFile(file, url);
        log.info("file :{}", file.getName());
        //上传到临时素材，返回mediaId
        String mediaId = mediaService.upload(file);
        if (mediaId != null) {
            //删除文件
            boolean delete = file.delete();
            log.info("删除文件{}: {}", file.getName(), delete);
        }
        return mediaId;
    }

    private void downFile(File file, String url) {
        URLConnection urlConnection = null;
        try {
            urlConnection = new URL(url).openConnection();
        } catch (IOException e) {
            log.error("打开链接url: {}，错误: {}", url, e.getMessage());
        }
        try (
            BufferedInputStream bufferedInputStream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
        ) {
            byte[] bytes = new byte[1024];
            int read;
            while ((read = bufferedInputStream.read(bytes)) != -1) {
                bufferedOutputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            log.error("文件读写错误: {}", e.getMessage());
        }
    }

    //##################### 京东API ########################

    /**
     * 叠蛋糕 - 查询我的金币
     */
    public String getHomeData(String openId) {
        String result = feignClient.getHomeData(openId);
        if (result != null) {
            result += "\n";
            result += feignClient.countCollectScore(openId, new Date());
        }
        return result;
    }

    /**
     * 叠蛋糕 - 统计领取金币
     */
    public String countCollectScore(String openId, Date date) {
        return feignClient.countCollectScore(openId, date);
    }

    //##################### 京东API ########################

    /**
     * 中国银行 - 查询信息
     */
    public String getBankChinaInfo(String openId) {
        String bankChinaInfo = feignClient.getBankChinaInfo(openId);
        return bankChinaInfo;
    }

    //##################### 通用日志 ########################

    /**
     * 查询通用日志
     */
    public List<ReqLog> getReqLogList(String openId, Integer platformType, Date date) {
        return feignClient.getReqLogList(openId, platformType, date);
    }

    //##################### BOSS ########################

    /**
     * BOSS - 批量下载简
     */
    public Map<String, Object> getResumeZip(String openId) {
        Map<String, Object> resp = feignClient.getResumeZip(openId);
        return resp;
    }

    /**
     * BOSS - 登录
     */
    public Map<String, Object> loginBoss(String openId) {
        Map<String, Object> resp = feignClient.loginBoss(openId);
        return resp;
    }

    /**
     * BOSS - 签收简历
     */
    public Map<String, Object> acceptResume(String openId) {
        Map<String, Object> resp = feignClient.acceptResume(openId);
        return resp;
    }

    /**
     * BOSS - 寻找牛人
     */
    public Map<String, Object> findGeek(String openId) {
        Map<String, Object> resp = feignClient.findGeek(openId);
        return resp;
    }
}
