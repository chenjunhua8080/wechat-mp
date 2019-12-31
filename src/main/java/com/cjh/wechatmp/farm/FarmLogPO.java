package com.cjh.wechatmp.farm;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 农场log
 *
 * @author chenjunhua
 * @email 1109551489@qq.com
 * @date 2019-12-10 16:13:23
 */
@Data
public class FarmLogPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Integer id;
    /**
     * openId
     */
    private String openId;
    /**
     * 信息
     */
    private String message;
    /**
     * 响应
     */
    private String resp;
    /**
     * 创建时间
     */
    private Date createTime;

}
