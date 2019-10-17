package com.cjh.wechatmp.sign;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class SignService {

    private MpProperty mpProperty;

    /**
     * 1）将token、timestamp、nonce三个参数进行字典序排序
     * 2）将三个参数字符串拼接成一个字符串进行sha1加密
     * 3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
     */
    public String sign(SignEntity signEntity) {
        String[] signParams = {signEntity.getNonce(), signEntity.getTimestamp(), mpProperty.getToken()};
        Arrays.sort(signParams);
        String str = StringUtils.join(signParams);
        String sha1Hex = DigestUtils.sha1Hex(str);
        String signature = signEntity.getSignature();
        return sha1Hex.equals(signature) ? signEntity.getEchostr() : null;
    }

}
