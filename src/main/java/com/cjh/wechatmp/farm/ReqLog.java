package com.cjh.wechatmp.farm;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 请求日志表(ReqLog)实体类
 *
 * @author cjh
 * @since 2020-04-03 13:47:28
 */
@Data
public class ReqLog implements Serializable {

    private static final long serialVersionUID = -17335203605332008L;

    private Integer id;
    private String userId;
    /**
     * 信息
     */
    private String message;
    /**
     * 响应
     */
    private String resp;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creteTime;

    /**
     * 绑定平台类型
     */
    private Integer platformType;

}