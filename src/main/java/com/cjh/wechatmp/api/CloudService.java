package com.cjh.wechatmp.api;

import com.alibaba.fastjson.JSONObject;
import com.cjh.wechatmp.avatar.AvatarPO;
import com.cjh.wechatmp.farm.FarmLogPO;
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
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
        return feignClient.getTodayFarmLog(farmOpenId);
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
}
