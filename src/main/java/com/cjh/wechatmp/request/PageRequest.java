package com.cjh.wechatmp.request;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest implements Serializable {

    private static final long serialVersionUID = -5355454567696138248L;

    /**
     * 页码
     */
    private int pageNum = 1;

    /**
     * 页大小
     */
    private int pageSize = 8;

}
