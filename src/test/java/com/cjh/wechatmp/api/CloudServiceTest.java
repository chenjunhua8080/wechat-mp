package com.cjh.wechatmp.api;

import com.cjh.wechatmp.avatar.AvatarPO;
import com.cjh.wechatmp.po.NowPlaying;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CloudServiceTest {

    @Autowired
    private CloudService cloudService;

    @Test
    public void getAvatarByNew() {
        List<AvatarPO> avatarByNew = cloudService.getAvatarByNew(1);
        assert !avatarByNew.isEmpty();
        log.info(String.valueOf(avatarByNew));
    }

    @Test
    public void getAvatar() {
        String mediaId = cloudService.getAvatar();
        log.info(mediaId);
    }

    @Test
    public void getNowPlayingList() {
        List<NowPlaying> nowPlayingList = cloudService.getNowPlayingList();
        log.info(String.valueOf(nowPlayingList));
    }


    @Test
    public void getRandJoke() {
        String randJoke = cloudService.getRandJoke();
        log.info(randJoke);
    }
}