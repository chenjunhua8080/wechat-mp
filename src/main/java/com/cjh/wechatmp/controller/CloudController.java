package com.cjh.wechatmp.controller;

import com.cjh.wechatmp.api.CloudService;
import com.cjh.wechatmp.po.NowPlaying;
import com.cjh.wechatmp.request.PageRequest;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/cloud")
public class CloudController {

    private CloudService cloudService;

    @GetMapping("/getNowPlaying")
    public NowPlaying getNowPlaying() {
        return cloudService.getNowPlaying();
    }

    @GetMapping("/getMovieDesc")
    public String getMovieDesc(String movieId) {
        return cloudService.getMovieDesc(movieId);
    }

    @GetMapping("/getComments")
    public List<String> getComments(@RequestParam("movieId") String movieId, PageRequest page) {
        return cloudService.getComments(movieId, page.getPageNum(), page.getPageSize());
    }

}
