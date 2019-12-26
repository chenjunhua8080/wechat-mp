package com.cjh.wechatmp.request;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author cjh
 * @email 
 * @date 2019-10-21 18:44:25
 */
@Data
public class UserRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Integer id;
	/**
	 * 
	 */
	private String openId;
	/**
	 * 
	 */
	private String name;
	/**
	 * 
	 */
	private Integer age;
	/**
	 * 
	 */
	private String phone;
	/**
	 * 
	 */
	private String password;

}
