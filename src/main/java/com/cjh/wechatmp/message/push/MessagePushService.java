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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
     * 推送模板消息[通用]
     */
    public String pushTempMsg(String openId, String body, String tempId) {
        String url = WxApi.TEMPLATE_SEND_BY_OPENID.replace("ACCESS_TOKEN", tokenService.getBaseToken());
        Temp temp;
        temp = new Temp();
        temp.setTemplate_id(tempId);
        temp.setTouser(openId);
        DataBean data = new DataBean();
        TextBean first = new TextBean();
        UserPO userPO = userDao.selectByOpenId(openId);
        first.setValue("您好：" + userPO.getName() + "\n");
        first.setColor("#459ae9");
        TextBean list = new TextBean();
        list.setValue(body + "\n");
        list.setColor("#173177");
        TextBean remark = new TextBean();
        remark.setValue("bye~" + "\n");
        remark.setColor("#f24d4d");
        data.setFirst(first);
        data.setList(list);
        data.setRemark(remark);
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
}
