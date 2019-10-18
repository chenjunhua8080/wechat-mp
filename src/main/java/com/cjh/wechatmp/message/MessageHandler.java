package com.cjh.wechatmp.message;

import com.alibaba.fastjson.JSONObject;
import com.cjh.wechatmp.message.in.TextInMessage;
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
        //转换成消息
        BaseMessage inMessage = MessageUtil.convert(xml);
        //避免重复处理
        if (messageService.isExist(inMessage)) {
            return null;
        }
        BaseMessage outMessage;
        if (MessageConstant.MESSAGE_TYPE_TEXT.equals(inMessage.getMsgType())) {
            String content = ((TextInMessage) inMessage).getContent();
            outMessage = MessageUtil.buildTextOutMessage(inMessage, content);
        } else {
            outMessage = MessageUtil.buildTextOutMessage(inMessage, "收到~");
        }
        String respXml = XmlUtil.java2xml(outMessage);

        //保存消息
        save(inMessage, MessageConstant.MESSAGE_IN);
        outMessage.setMsgId(inMessage.getMsgId());
        save(outMessage, MessageConstant.MESSAGE_OUT);

        return respXml;
    }

    /**
     * 保存消息
     */
    private void save(BaseMessage baseMessage, int inOrOut) {
        MessagePO inMessagePO = new MessagePO();
        inMessagePO.setInOut(inOrOut);
        inMessagePO.setMsgId(baseMessage.getMsgId());
        inMessagePO.setType(baseMessage.getMsgType());
        inMessagePO.setBody(JSONObject.toJSONString(baseMessage));
        messageService.save(inMessagePO);
    }

}
