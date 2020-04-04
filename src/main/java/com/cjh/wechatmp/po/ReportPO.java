package com.cjh.wechatmp.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 那啥次数统计
 */
@Data
@TableName("report")
public class ReportPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Integer id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 创建时间
     */
    private Date creteTime;

}
