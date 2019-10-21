package com.cjh.wechatmp.controller;

import com.cjh.wechatmp.token.JsSDKTokenDTO;
import com.cjh.wechatmp.token.OAuth2TokenDTO;
import com.cjh.wechatmp.token.TokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/token")
public class TokenController {

    private TokenService tokenService;

    @GetMapping("/getOAuth2Token/{code}")
    public OAuth2TokenDTO getOAuth2Token(@PathVariable String code) {
        log.info("****************通过code换取网页授权access_token**********************");
        OAuth2TokenDTO oAuth2Token = tokenService.getOAuth2Token(code);
        log.info("****************通过code换取网页授权access_token结束*******************");
        return oAuth2Token;
    }

    @GetMapping("/getJsSDKToken")
    public JsSDKTokenDTO getJsSDKTokenDTO(@RequestParam String url) {
        log.info("****************获取jsSDK授权token**********************");
        JsSDKTokenDTO jsSDKTokenDTO = tokenService.getJsSDKTokenDTO(url);
        log.info("****************获取jsSDK授权token结束******************");
        return jsSDKTokenDTO;
    }

}
