package com.cjh.wechatmp.message.push;

import com.alibaba.fastjson.JSONObject;
import com.cjh.wechatmp.api.WxApi;
import com.cjh.wechatmp.dao.UserDao;
import com.cjh.wechatmp.exception.WxErrorEntity;
import com.cjh.wechatmp.message.push.Entity.TextBean;
import com.cjh.wechatmp.po.UserPO;
import com.cjh.wechatmp.token.TokenService;
import com.cjh.wechatmp.util.JsonUtil;
import com.cjh.wechatmp.util.RestTemplateUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class MessagePushService {

    private TokenService tokenService;
    private UserDao userDao;

    /**
     * 根据OpenID列表群发【订阅号不可用，服务号认证后可用】
     */
    public String pushTextByOpenId(String text) {
        String url = WxApi.MESSAGE_SEND_BY_OPENID.replace("ACCESS_TOKEN", tokenService.getBaseToken());
        Entity entity = new Entity();
        ArrayList<String> openIds = new ArrayList<>();
        List<UserPO> userPOS = userDao.selectList(null);
        for (UserPO userPO : userPOS) {
            openIds.add(userPO.getOpenId());
        }
        entity.setTouser(openIds);
        entity.setMsgtype("text");
        TextBean textBean = new TextBean();
        textBean.setContent(text);
        entity.setText(textBean);
        String resp = RestTemplateUtil.doPost(url, JSONObject.toJSONString(entity));
        WxErrorEntity errorEntity = JsonUtil.json2java(resp, WxErrorEntity.class);
        if (errorEntity.getErrcode() == 0) {
            log.info("推送消息成功: {}", errorEntity.getMsg_id());
        } else {
            log.info("推送消息成功: {}", resp);
        }
        return resp;
    }

}
