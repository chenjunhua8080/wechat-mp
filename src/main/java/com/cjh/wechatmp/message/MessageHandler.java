package com.cjh.wechatmp.message;

import com.alibaba.fastjson.JSONObject;
import com.cjh.wechatmp.po.MessagePO;
import com.cjh.wechatmp.service.MessageService;
import com.cjh.wechatmp.util.XmlUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class MessageHandler {

    private MessageService messageService;

    /**
     * 处理消息
     */
    public String handle(String xml) {
        //处理消息
        JSONObject jsonObject = XmlUtil.xml2json(xml);
        InTextMessageIn inTextMessage = jsonObject.toJavaObject(InTextMessageIn.class);
        //避免重复处理
        if (messageService.isExist(inTextMessage.getMsgId())) {
            return null;
        }
        OutTextMessageIn outTextMessage = new OutTextMessageIn();
        outTextMessage.setCreateTime(String.valueOf(System.currentTimeMillis()));
        outTextMessage.setMsgType(MessageConstant.MESSAGE_TYPE_TEXT);
        outTextMessage.setFromUserName(inTextMessage.getToUserName());
        outTextMessage.setToUserName(inTextMessage.getFromUserName());
        if (MessageConstant.MESSAGE_TYPE_TEXT.equals(inTextMessage.getMsgType())) {
            outTextMessage.setContent(inTextMessage.getContent());
        } else {
            outTextMessage.setContent("嗯嗯");
        }
        String respXml = XmlUtil.java2xml(outTextMessage);

        //保存消息
        save(inTextMessage, outTextMessage);

        return respXml;
    }

    /**
     * 保存消息
     */
    private void save(InTextMessageIn inTextMessage, OutTextMessageIn outTextMessage) {
        //保存接收消息
        MessagePO inMessagePO = new MessagePO();
        inMessagePO.setMsgId(inTextMessage.getMsgId());
        inMessagePO.setInOut(1);
        inMessagePO.setType(inTextMessage.getMsgType());
        inMessagePO.setBody(JSONObject.toJSONString(inTextMessage));
        messageService.save(inMessagePO);
        //保存响应消息
        MessagePO outMessagePO = new MessagePO();
        outMessagePO.setMsgId(inTextMessage.getMsgId());
        outMessagePO.setInOut(2);
        outMessagePO.setType(outTextMessage.getMsgType());
        outMessagePO.setBody(JSONObject.toJSONString(outTextMessage));
        messageService.save(outMessagePO);
    }

}
