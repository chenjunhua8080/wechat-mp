package com.cjh.wechatmp.handler;

import com.cjh.wechatmp.annotation.MessageProcessor;
import com.cjh.wechatmp.enums.InstructsEnum;
import com.cjh.wechatmp.message.BaseMessage;
import com.cjh.wechatmp.message.MessageConstant;
import com.cjh.wechatmp.message.MessageUtil;
import com.cjh.wechatmp.message.handler.AbstractMessageHandler;
import com.cjh.wechatmp.message.in.EventMessage;
import com.cjh.wechatmp.redis.RedisConstant;
import com.cjh.wechatmp.redis.RedisService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Click事件消息处理器
 */
@AllArgsConstructor
@MessageProcessor(messageType = MessageConstant.MESSAGE_TYPE_EVENT, eventType = MessageConstant.EVENT_TYPE_CLICK)
@Component
public class ClickEventHandler extends AbstractMessageHandler {

    private RedisService redisService;

    @Override
    public BaseMessage doHandle(BaseMessage inMessage) {
        EventMessage eventMessage = (EventMessage) inMessage;
        String eventKey = eventMessage.getEventKey();
        String content;
        if (eventKey.equals("menu_1")) {
            String userToken = redisService.get(RedisConstant.USER_TOKEN+eventMessage.getFromUserName());
            if (userToken != null) {
                content = "已绑定";
            } else {
                content = "未登录哦！<a href=\"http://h5.springeasy.cn/pages/user/user\">戳我</a>去登陆";
            }
            return MessageUtil.buildTextOutMessage(inMessage, content);
        }else  if (eventKey.equals("menu_2_1")) {
            //清除旧指令
            redisService.getLastInstruct(inMessage.getFromUserName(), true);
            String instructs = InstructsEnum.getInstructs(0);
            return MessageUtil.buildTextOutMessage(inMessage, instructs);
        }
        return null;
    }
}
