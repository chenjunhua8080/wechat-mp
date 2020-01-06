package com.cjh.wechatmp.media;

import com.cjh.wechatmp.api.WxApi;
import com.cjh.wechatmp.token.TokenService;
import com.cjh.wechatmp.util.JsonUtil;
import com.cjh.wechatmp.util.RestTemplateUtil;
import java.io.File;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@AllArgsConstructor
@Component
@Slf4j
public class MediaService {

    private TokenService tokenService;

    /**
     * 新增临时素材
     */
    public String upload(File file) {
        String url = WxApi.MEDIA_UPLOAD.replace("ACCESS_TOKEN", tokenService.getBaseToken())
            .replace("TYPE", "image");
        //获取上传到临时素材
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setConnection("Keep-Alive");
        headers.setCacheControl("no-cache");
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        FileSystemResource resource = new FileSystemResource(file);
        body.add("media", resource);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);
        String resp = RestTemplateUtil.getRestTemplate().postForObject(url, httpEntity, String.class);
        MediaEntity entity = JsonUtil.json2java(resp, MediaEntity.class);
        assert entity != null;
        String mediaId = entity.getMedia_id();
        if (mediaId != null) {
            log.info("新增临时素材成功，media: {}", mediaId);
        } else {
            log.info("新增临时素材失败: {}", resp);
        }
        return mediaId;
    }

}
