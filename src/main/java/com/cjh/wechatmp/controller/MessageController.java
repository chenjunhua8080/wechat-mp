package com.cjh.wechatmp.controller;

import com.cjh.wechatmp.message.BaseMessage;
import com.cjh.wechatmp.message.MessageHandler;
import com.cjh.wechatmp.message.MessageUtil;
import com.cjh.wechatmp.message.handler.AbstractMessageHandler;
import com.cjh.wechatmp.message.handler.MessageHandlerAdapter;
import com.cjh.wechatmp.util.XmlUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/message")
public class MessageController {

    private MessageHandler messageHandler;
    private MessageHandlerAdapter messageHandlerAdapter;

    @PostMapping("/in")
    public String sign(HttpServletRequest request) {
        log.info("*****************收到微信服务器推送消息******************");
        //客户端的IP地址
        log.info("Remote Addr: {}", request.getRemoteAddr());
        //请求的字符编码
        log.info("Character Encoding: {}", request.getCharacterEncoding());
        //请求的消息体（body）的大小 字节数，没有消息体的，返回-1
        log.info("Content Length: {}", request.getContentLength());
        //返回请求的消息体的MIME类型，MIME是描述消息内容类型的因特网标准
        log.info("Content Type: {}", request.getContentType());
        //获取请求方式获取请求方式(GET与POST为主,也会有PUT、DELETE、INPUT)
        log.info("HTTP Method: {}", request.getMethod());
        //获取完整的url，不带参数的
        log.info("Request URI: {}", request.getRequestURI());
        //获取url中参数的部分
        log.info("Query String: {}", request.getQueryString());
        //获取body
        BufferedReader reader;
        String body = null;
        try {
            reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            body = IOUtils.toString(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("body: {}", body);
//        String resp = messageHandler.handle(body);
        String msgType = MessageUtil.getMessageType(body);
        AbstractMessageHandler messageHandler = messageHandlerAdapter.findMessageHandler(msgType);
        BaseMessage outMessage = messageHandler.handleMessage(MessageUtil.convert(body));
        String resp = null;
        if (outMessage != null) {
            resp = XmlUtil.java2xml(outMessage);
        }
        log.info("待响应: {}", resp);
        log.info("**********************消息处理结束**********************");
        return resp;
    }

}
