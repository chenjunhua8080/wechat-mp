package com.cjh.wechatmp.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cjh.wechatmp.avatar.AvatarPO;
import com.cjh.wechatmp.farm.FarmLogPO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@AllArgsConstructor
public class CloudService {

    private RestTemplate restTemplate;
    private MediaService mediaService;

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

    /**
     * 头像列表
     */
    public List<AvatarPO> getAvatarByNew(int pageNum) {
        String url = CloudApi.HOST_COMMON + "getAvatarByNew?pageNum=" + pageNum;
        log.info("服务调用: {}", url);
        String forObject = restTemplate.getForObject(url, String.class);
        JSONArray jsonArray = JSONArray.parseArray(forObject);
        log.info("服务结果: {}", forObject);
        return jsonArray != null ? jsonArray.toJavaList(AvatarPO.class) : new ArrayList<>();
    }

    /**
     * 头像图片
     */
    public String getAvatar() {
        //获取图片链接
        List<AvatarPO> list = getAvatarByNew((int) (Math.random() * 10));
        if (list.isEmpty()) {
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
}
