package com.cjh.wechatmp.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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
@TableName("message")
public class MessagePO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
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
	private String type;
	/**
	 *
	 */
	private String msgId;

	/**
	 * 
	 */
	private String body;

}
