package com.cjh.wechatmp.message.push;

import com.alibaba.fastjson.JSONObject;
import com.cjh.wechatmp.api.WxApi;
import com.cjh.wechatmp.dao.UserDao;
import com.cjh.wechatmp.exception.WxErrorEntity;
import com.cjh.wechatmp.message.push.Temp.DataBean;
import com.cjh.wechatmp.message.push.Temp.DataBean.TextBean;
import com.cjh.wechatmp.po.UserPO;
import com.cjh.wechatmp.token.TokenService;
import com.cjh.wechatmp.util.JsonUtil;
import com.cjh.wechatmp.util.RestTemplateUtil;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@RefreshScope
@Slf4j
@Component
public class MessagePushService {

    @Value("${mp.temp.farm}")
    private String farmTemp;

    @Autowired
    private TokenService tokenService;
    @Resource
    private UserDao userDao;


    /**
     * 根据OpenID列表群发【订阅号不可用，服务号认证后可用】
     */
    public String pushTextByOpenId(String text) {
        String url = WxApi.MESSAGE_SEND_BY_OPENID.replace("ACCESS_TOKEN", tokenService.getBaseToken());
        ArrayList<String> openIds = new ArrayList<>();
        List<UserPO> userPOS = userDao.selectList(null);
        for (UserPO userPO : userPOS) {
            openIds.add(userPO.getOpenId());
        }
        Message message = new Message();
        message.setTouser(openIds);
        message.setMsgtype("text");
        Message.TextBean textBean = new Message.TextBean();
        textBean.setContent(text);
        message.setText(textBean);
        String resp = RestTemplateUtil.doPost(url, JSONObject.toJSONString(message));
        WxErrorEntity errorEntity = JsonUtil.json2java(resp, WxErrorEntity.class);
        if (errorEntity.getErrcode() == 0) {
            log.info("推送消息成功: {}", errorEntity.getMsg_id());
        } else {
            log.info("推送消息失败: {}", resp);
        }
        return resp;
    }

    /**
     * 推送模板消息
     */
    public String pushTempByOpenId(String openId, String body) {
        String url = WxApi.TEMPLATE_SEND_BY_OPENID.replace("ACCESS_TOKEN", tokenService.getBaseToken());
        Temp temp;
        temp = new Temp();
        temp.setTemplateId(farmTemp);
        temp.setTouser(openId);
        DataBean data = new DataBean();
        TextBean first = new TextBean();
        first.setValue("今天农场操作日志");
        first.setColor("#173177");
        TextBean list = new TextBean();
        list.setValue(body);
        list.setColor("#173177");
        TextBean remark = new TextBean();
        remark.setValue("bye");
        remark.setColor("#173177");
        data.setFirst(first);
        data.setList(list);
        data.setRemark(remark);
        temp.setData(data);

        String resp = RestTemplateUtil.doPost(url, JSONObject.toJSONString(temp));
        WxErrorEntity errorEntity = JsonUtil.json2java(resp, WxErrorEntity.class);
        if (errorEntity.getErrcode() == 0) {
            log.info("推送模板成功: {}", errorEntity.getMsgid());
        } else {
            log.info("推送模板失败: {}", resp);
        }
        return resp;
    }

}
