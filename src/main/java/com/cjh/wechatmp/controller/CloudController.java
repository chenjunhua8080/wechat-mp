package com.cjh.wechatmp.controller;

import com.cjh.wechatmp.api.CloudService;
import com.cjh.wechatmp.request.PageRequest;
import com.cjh.wechatmp.response.Result;
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
    public Result getNowPlaying() {
        return Result.success().setData(cloudService.getNowPlaying());
    }

    @GetMapping("/getMovieDesc")
    public Result getMovieDesc(String movieId) {
        String movieDesc = cloudService.getMovieDesc(movieId);
        return Result.success().setData(movieDesc);
    }

    @GetMapping("/getComments")
    public Result getComments(@RequestParam("movieId") String movieId, PageRequest page) {
        return Result.success().setData(cloudService.getComments(movieId, page.getPageNum(), page.getPageSize()));
    }

}
