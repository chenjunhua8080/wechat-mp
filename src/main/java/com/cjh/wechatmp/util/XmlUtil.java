package com.cjh.wechatmp.util;

import com.alibaba.fastjson.JSONObject;
import com.cjh.wechatmp.po.message.in.TextMessage;
import com.google.gson.Gson;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class XmlUtil {

    /**
     * java2xml
     * 默认节点"xml"
     * 首字母大写
     */
    public static String java2xml(Object object) {
        return java2xml(object, "xml", true);
    }

    /**
     * java2xml
     */
    public static String java2xml(Object object, String rootName, boolean firstBig) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement(rootName);
        Gson gson = new Gson();
        String jsonStr = gson.toJson(object);
        recursion(root, JSONObject.parseObject(jsonStr), firstBig);
        return document.getRootElement().asXML();
    }

    /**
     * 递归处理jsonObject，添加到root上
     */
    private static void recursion(Element root, JSONObject jsonObject, boolean firstBig) {

        for (String item : jsonObject.keySet()) {
            String key = item;
            Object value = jsonObject.get(item);
            //首字母大写
            if (firstBig) {
                key = item.replace(item.substring(0, 1), item.substring(0, 1).toUpperCase());
            }
            Element element;
            if (value instanceof JSONObject) {
                element = root.addElement(key);
                recursion(element, (JSONObject) value, firstBig);
            } else {
                element = root.addElement(key);
                if (!(value instanceof Number)) {
                    element.addCDATA(String.valueOf(value));
                } else {
                    element.addText(String.valueOf(value));
                }
            }
        }

    }

    /**
     * xml2json
     */
    public static <T> JSONObject xml2json(String xml) {

        JSONObject jsonObject = null;
        try {
            Document document = DocumentHelper.parseText(xml);
            Element rootElement = document.getRootElement();
            jsonObject = new JSONObject();
            recursion(jsonObject, rootElement.elements());
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        System.out.println(jsonObject);
        return jsonObject;
    }

    /**
     * xml2java
     */
    public static <T> T xml2java(String xml, Class<T> clazz) {
        JSONObject jsonObject = new JSONObject();

        try {
            Document document = DocumentHelper.parseText(xml);
            Element rootElement = document.getRootElement();
            recursion(jsonObject, rootElement.elements());
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        System.out.println(jsonObject);
        return jsonObject.toJavaObject(clazz);
    }

    /**
     * 递归处理elements，添加到jsonObject
     */
    private static void recursion(JSONObject jsonObject, List<Element> elements) {

        for (Element element : elements) {
            //首字母小写
            String key = element.getName();
            key = key.replace(key.substring(0, 1), key.substring(0, 1).toLowerCase());

            if (!element.isTextOnly()) {
                JSONObject subNode = new JSONObject();
                recursion(subNode, element.elements());
                jsonObject.put(key, subNode);
            } else {
                jsonObject.put(key, element.getText());
            }
        }

    }

    public static void main(String[] args) {
        String xml = "<xml>\n  <ToUserName><![CDATA[toUser]]></ToUserName>\n  <FromUserName><![CDATA[fromUser]]></FromUserName>\n  <CreateTime>1348831860</CreateTime>\n  <MsgType><![CDATA[text]]></MsgType>\n  <Content><![CDATA[this is a test]]></Content>\n  <MsgId>1234567890123456</MsgId>\n  <user>\n  <name>zhangsan</name>\n  <age>18</age>\n  </user>\n</xml>";
        TextMessage inMessageText = xml2java(xml, TextMessage.class);
        System.out.println(inMessageText.getMsgType());
        xml = java2xml(inMessageText);
        System.out.println(xml);
    }
}
