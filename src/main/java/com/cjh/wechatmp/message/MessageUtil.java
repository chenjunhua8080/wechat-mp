package com.cjh.wechatmp.message;

import com.cjh.wechatmp.exception.ServiceException;
import com.cjh.wechatmp.message.in.EventMessage;
import com.cjh.wechatmp.message.in.ImgInMessage;
import com.cjh.wechatmp.message.in.TextInMessage;
import com.cjh.wechatmp.message.out.Image;
import com.cjh.wechatmp.message.out.ImgOutMessage;
import com.cjh.wechatmp.message.out.TextOutMessage;
import com.cjh.wechatmp.util.XmlUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息工具类
 */
@Slf4j
public class MessageUtil {

    /**
     * 解析消息类型字段的正则表达式
     */
    private static final Pattern MESSAGE_TYPE_PATTERN = Pattern
        .compile("\\<MsgType\\>\\<\\!\\[CDATA\\[(.*?)\\]\\]\\>\\<\\/MsgType\\>");

    /**
     * 解析事件类型字段的正则表达式
     */
    private static final Pattern EVENT_TYPE_PATTERN = Pattern
        .compile("\\<Event\\>\\<\\!\\[CDATA\\[(.*?)\\]\\]\\>\\<\\/Event\\>");

    /**
     * 获取消息类型
     */
    public static String getMessageType(String xml) {
        Matcher matcher = MESSAGE_TYPE_PATTERN.matcher(xml);
        return matcher.find() ? matcher.group(1) : null;
    }

    /**
     * 获取事件类型
     */
    public static String getEventType(String xml) {
        Matcher matcher = EVENT_TYPE_PATTERN.matcher(xml);
        return matcher.find() ? matcher.group(1) : null;
    }

    /**
     * xml转成对应的消息
     */
    public static BaseMessage convert(String xml) {
        if (log.isInfoEnabled()) {
            log.info("xml -> {}", xml);
        }
        String messageType = MessageUtil.getMessageType(xml);
        if (messageType == null) {
            log.error("xml中为找未匹配到messageType");
            throw new ServiceException("xml中为找未匹配到messageType");
        }
        switch (messageType) {
            //文本消息
            case MessageConstant.MESSAGE_TYPE_TEXT:
                return XmlUtil.xml2java(xml, TextInMessage.class);
            //图片消息
            case MessageConstant.MESSAGE_TYPE_IMG:
                return XmlUtil.xml2java(xml, ImgInMessage.class);
            //事件消息
            case MessageConstant.MESSAGE_TYPE_EVENT:
                return XmlUtil.xml2java(xml, EventMessage.class);
            default:
                log.error("未知消息类型: {}" + messageType);
                throw new ServiceException("未知消息类型: " + messageType);
        }
    }

    /**
     * 构建<strong>文本</strong>响应消息
     */
    public static TextOutMessage buildTextOutMessage(BaseMessage requestMessage, String content) {
        TextOutMessage textResponseMessage = new TextOutMessage();
        textResponseMessage.setCreateTime(System.currentTimeMillis());
        textResponseMessage.setFromUserName(requestMessage.getToUserName());
        textResponseMessage.setToUserName(requestMessage.getFromUserName());
        textResponseMessage.setMsgType(MessageConstant.MESSAGE_TYPE_TEXT);
        textResponseMessage.setContent(content);
        return textResponseMessage;
    }

    /**
     * 构建<strong>图片</strong>响应消息
     */
    public static ImgOutMessage buildImgOutMessage(BaseMessage requestMessage, String mediaId) {
        ImgOutMessage imgOutMessage = new ImgOutMessage();
        imgOutMessage.setCreateTime(System.currentTimeMillis());
        imgOutMessage.setToUserName(requestMessage.getFromUserName());
        imgOutMessage.setFromUserName(requestMessage.getToUserName());
        imgOutMessage.setMsgType(MessageConstant.MESSAGE_TYPE_IMG_RESP);
        Image image = new Image();
        image.setMediaId(mediaId);
        imgOutMessage.setImage(image);
        return imgOutMessage;
    }
}
