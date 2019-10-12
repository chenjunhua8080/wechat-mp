package com.cjh.wechatmp.service;

import com.alibaba.fastjson.JSONObject;
import com.cjh.wechatmp.po.message.in.TextMessage;
import com.cjh.wechatmp.util.XmlUtil;
import org.springframework.stereotype.Component;

@Component
public class MessageService {

    public String handleMessage(String xml) {
        JSONObject jsonObject = XmlUtil.xml2json(xml);
        TextMessage textMessage = jsonObject.toJavaObject(TextMessage.class);
        if (jsonObject.getString("msgType").equals("text")) {

        } else {
            textMessage.setContent("嗯嗯");
        }
        textMessage.setMsgType("text");
        textMessage.setFromUserName(jsonObject.getString("toUserName"));
        textMessage.setToUserName(jsonObject.getString("fromUserName"));
        return XmlUtil.java2xml(textMessage);
    }

}
