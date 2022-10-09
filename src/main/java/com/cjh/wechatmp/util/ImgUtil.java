package com.cjh.wechatmp.util;

import cn.hutool.core.codec.Base64Encoder;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

/**
 * 图片工具类
 */
@Slf4j
public class ImgUtil {

    /**
     * 将图片转换成base64格式进行存储
     */
    public static String img2base64(String url) {
        String type = StringUtils.substring(url, url.lastIndexOf(".") + 1);
        BufferedImage image = null;
        try {
            image = ImageIO.read(new URL(url));
        } catch (Exception e) {
            log.info("url: {}", url);
            log.error("img2base64转换失败: {}", e.getMessage());
        }
        String imageString = "data:image/jpeg;base64,";
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();
            imageString += Base64Encoder.encode(imageBytes);
            bos.close();
        } catch (IOException e) {
            log.error("img2base64转换失败: {}", e.getMessage());
        }
        return imageString;
    }

}
