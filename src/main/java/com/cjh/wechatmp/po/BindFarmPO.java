package com.cjh.wechatmp.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 绑定农场
 * 
 * @author chenjunhua
 * @email 1109551489@qq.com
 * @date 2019-12-10 18:00:04
 */
@Data
@TableName("bind_farm")
public class BindFarmPO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 用户id
	 */
	private Integer userId;
	/**
	 * 农场openId
	 */
	private String farmOpenid;

}
