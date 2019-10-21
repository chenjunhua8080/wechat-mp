package com.cjh.wechatmp.message.handler;

import com.cjh.wechatmp.annotation.MessageProcessor;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * spring容器-消息处理器适配器
 * <p>
 * 在spring容器中寻找消息处理器适配器
 */
@Slf4j
@Component
public class SpringMessageHandlerAdapter implements MessageHandlerAdapter, ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public AbstractMessageHandler findMessageHandler(String messageType, String eventType) {
        log.info("寻找 {} 消息处理器", messageType);

        //获取到所有拥有特定注解的Beans集合
        Map<String, Object> beans = this.context.getBeansWithAnnotation(MessageProcessor.class);
        if (beans.isEmpty()) {
            log.error("没有找到 {} 消息处理器", messageType);
            return null;
        }
        log.info("找到消息处理器 {}", beans);

        for (String beanName : beans.keySet()) {
            Object annotationBean = beans.get(beanName);
            Class<?> annotationBeanClass = annotationBean.getClass();
            //消息处理器类必须是abstractMessageHandler的子类
            if (!AbstractMessageHandler.class.isAssignableFrom(annotationBeanClass)) {
                continue;
            }
            MessageProcessor annotation = annotationBeanClass.getAnnotation(MessageProcessor.class);
            //精准匹配处理器
            if (messageType.equals(annotation.messageType())) {
                if (eventType == null) {
                    //消息处理器
                    log.info("返回消息处理器 {}", annotationBean);
                    return (AbstractMessageHandler) annotationBean;
                } else {
                    //事件处理器
                    if (eventType.equals(annotation.eventType())) {
                        log.info("返回事件处理器 {}", annotationBean);
                        return (AbstractMessageHandler) annotationBean;
                    }
                }
            }
        }

        log.error("没有找到 {} 消息处理器", messageType);
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

}
