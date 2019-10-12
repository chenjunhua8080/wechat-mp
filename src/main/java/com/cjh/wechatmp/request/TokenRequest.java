package com.cjh.wechatmp.request;

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
public class TokenRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
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
