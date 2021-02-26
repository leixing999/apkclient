package com.shxp.apk.domain.po;

import lombok.Data;

import java.io.Serializable;


/**
 * app对应权限列表
 *
 * @author xinglei
 * @date 2021-02-26 16:11:46
 */
@Data
public class AppPermission implements Serializable {

	/**
	 * 自增量
	 * nullable : false
	 * default  : null
	 */
	private String id;

	/**
	 * 关联app_telecom_link表
	 * nullable : true
	 * default  : null
	 */
	private String appLinkId;

	/**
	 * app对应的权限名称
	 * nullable : true
	 * default  : null
	 */
	private String appPermissionName;
}
