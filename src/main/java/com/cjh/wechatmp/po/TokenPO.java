package com.cjh.wechatmp.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author cjh
 * @email 
 * @date 2019-10-11 15:10:33
 */
@Data
@TableName("token")
public class TokenPO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 
	 */
	private Date createDate;
	/**
	 * 
	 */
	private Integer time;
	/**
	 * 
	 */
	private String value;

}
