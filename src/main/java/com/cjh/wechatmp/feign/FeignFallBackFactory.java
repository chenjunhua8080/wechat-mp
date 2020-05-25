package com.cjh.wechatmp.feign;

import com.alibaba.fastjson.JSONObject;
import com.cjh.wechatmp.avatar.AvatarPO;
import com.cjh.wechatmp.farm.FarmLogPO;
import com.cjh.wechatmp.juhe.QuestionBankPO;
import com.cjh.wechatmp.po.NowPlaying;
import feign.hystrix.FallbackFactory;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FeignFallBackFactory implements FallbackFactory<CloudFeignClient> {

    @Override
    public CloudFeignClient create(Throwable throwable) {

        log.error("in FeignFallBackFactory...");
        throwable.printStackTrace();

        return new CloudFeignClient() {

            @Override
            public String getReqLogList(String openId, Integer platformType, Date date) {
                return null;
            }

            @Override
            public List<AvatarPO> getAvatarByNew(Integer pageNum) {
                return null;
            }

            @Override
            public List<FarmLogPO> getTodayFarmLog(String openId, Date date) {
                return null;
            }

            @Override
            public List<NowPlaying> getNowPlaying() {
                return null;
            }

            @Override
            public String getMovieDesc(String id) {
                return null;
            }

            @Override
            public List<String> getComments(String id, Integer pageNum, Integer pageSize) {
                return null;
            }

            @Override
            public String getTodayHistory() {
                return null;
            }

            @Override
            public String getRandJoke() {
                return null;
            }

            @Override
            public String getSimpleWeadther(String city) {
                return null;
            }

            @Override
            public String getConstellation(String start) {
                return null;
            }

            @Override
            public QuestionBankPO getQuestionBank() {
                return null;
            }

            @Override
            public JSONObject getQuestionAnswers() {
                return null;
            }

            @Override
            public String getHomeData(String openId) {
                return null;
            }

            @Override
            public String countCollectScore(String openId, Date date) {
                return null;
            }

            @Override
            public String getBankChinaInfo(String openId) {
                return null;
            }
        };
    }
}
