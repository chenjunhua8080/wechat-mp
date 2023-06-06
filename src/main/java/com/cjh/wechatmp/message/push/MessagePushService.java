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
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@AllArgsConstructor
@Component
@Slf4j
public class MessagePushService {

    private TemplateConfig templateConfig;
    private TokenService tokenService;
    private UserDao userDao;

    /**
     * 根据OpenID列表群发【订阅号不可用，服务号认证后可用】
     */
    public String pushTextByOpenId(List<String> openIds, String text) {
        String url = WxApi.MESSAGE_SEND_BY_OPENID.replace("ACCESS_TOKEN", tokenService.getBaseToken());
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
     * 推送模板消息[通用]
     */
    public String pushTempMsg(String openId, String body, String tempId, String link) {
        String url = WxApi.TEMPLATE_SEND_BY_OPENID.replace("ACCESS_TOKEN", tokenService.getBaseToken());
        Temp temp;
        temp = new Temp();
        if (!StringUtils.isEmpty(link)) {
            temp.setUrl(link);
        }
        temp.setTemplate_id(tempId);
        temp.setTouser(openId);
        DataBean data = new DataBean();
        TextBean text1 = new TextBean();
        UserPO userPO = userDao.selectByOpenId(openId);
        text1.setValue("text1");
//        text1.setColor("#459ae9");
        TextBean text2 = new TextBean();
        text2.setValue(userPO.getName());
//        text2.setColor("#f24d4d");
        TextBean text3 = new TextBean();
        text3.setValue(body);
//        text3.setColor("#f24d4d");
//        TextBean text4 = new TextBean();
//        text4.setValue("text4"+"\n\n");
//        text3.setColor("#f24d4d");
//        TextBean text5 = new TextBean();
//        text5.setValue("text5");
//        text3.setColor("#f24d4d");
        TextBean text6 = new TextBean();
        text6.setValue("bye~");
        text3.setColor("#f24d4d");
        data.setText1(text1);
        data.setText2(text2);
//        data.setText3(text3);
//        data.setText4(text4);
//        data.setText5(text5);
        data.setText6(text6);
        temp.setData(data);

        String resp = RestTemplateUtil.doPost(url, JSONObject.toJSONString(temp));
        WxErrorEntity errorEntity = JsonUtil.json2java(resp, WxErrorEntity.class);
        if (errorEntity.getErrcode() == 0) {
            log.info("推送模板消息[{}]成功: {}", tempId, errorEntity.getMsgid());
        } else {
            log.info("推送模板消息[{}]失败: {}", tempId, resp);
        }
        return resp;
    }

    /**
     * 推送模板消息[通用，不带link]
     */
    public String pushTempMsg(String openId, String body, String tempId) {
        return pushTempMsg(openId, body, tempId, null);
    }

    /**
     * 推送模板消息[自我报告]
     */
    public String pushReportMsg(String openId, String body) {
        return pushTempMsg(openId, body, templateConfig.getReport());
    }

    /**
     * 推送模板消息[异常通知]
     */
    public String pushErrorMsg(String openId, String body) {
        return pushTempMsg(openId, body, templateConfig.getError());
    }

    /**
     * 推送模板消息[定时任务通知	]
     */
    public String pushJobMsg(String openId, String body) {
        return pushTempMsg(openId, body, templateConfig.getJob());
    }

    /**
     * 推送模板消息[简历下载通知]
     */
    public String pushResumeMsg(String openId, String body, String link) {
        return pushTempMsg(openId, body, templateConfig.getResume(), link);
    }
}
