package com.cjh.wechatmp.request;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 消息表
 * 
 * @author cjh
 * @email 
 * @date 2019-10-14 11:23:26
 */
@Data
public class MessageRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Integer id;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private Date updateTime;
	/**
	 * 0有效，1无效
	 */
	private Integer disable;
	/**
	 * 1进，2出
	 */
	private Integer inOut;
	/**
	 * 
	 */
	private Integer type;
	/**
	 * 
	 */
	private String body;

}
