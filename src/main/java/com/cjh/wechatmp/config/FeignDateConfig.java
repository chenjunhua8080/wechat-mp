package com.cjh.wechatmp.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@Configuration
public class FeignDateConfig {

    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @PostConstruct
    public void initFeignDateReceive() {
        ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer) handlerAdapter
            .getWebBindingInitializer();
        if (initializer.getConversionService() != null) {
            GenericConversionService genericConversionService = (GenericConversionService) initializer
                .getConversionService();
            genericConversionService.addConverter(String.class, Date.class, new String2DateConverter());
        }
    }

    @Bean
    public FeignFormatterRegistrar initFeignDateSend() {
        return formatterRegistry -> formatterRegistry
            .addConverter(Date.class, String.class, new Date2StringConverter());
    }

    private static class String2DateConverter implements Converter<String, Date> {

        @Override
        public Date convert(String source) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                return sdf.parse(source);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class Date2StringConverter implements Converter<Date, String> {

        @Override
        public String convert(Date source) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(source);
        }
    }
}
