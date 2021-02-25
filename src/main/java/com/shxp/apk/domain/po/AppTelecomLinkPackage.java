package com.shxp.apk.domain.po;


import lombok.Data;

import java.io.Serializable;


/**
 * 电信app链接包
 *
 * @author xinglei
 * @date 2021-02-24 18:20:01
 */
@Data
public class AppTelecomLinkPackage implements Serializable {

	/**
	 * 自增量
	 */
	private String id;

	/**
	 * 包含app链接的文件名
	 */
	private String linkPackageName;

	/**
	 * 上传者IP地址
	 */
	private String ip;

	/**
	 * 包含app 链接的包大小
	 */
	private Long linkPackageSize;

	/**
	 * 包状态(0:待处理，1：处理中，2：处理完成）
	 */
	private Integer linkPackageStatus;

	/**
	 * 包总行数
	 */
	private Long linkPackageLines;

	/**
	 * 目前分析所在行
	 */
	private Long linkPackageParseLine;

	/**
	 * 添加时间
	 */
	private java.util.Date linkPackageAddTime;

	/**
	 * 包分析开始时间
	 */
	private java.util.Date linkPackageParseBeginTime;

	/**
	 * 包分析结束时间
	 */
	private java.util.Date linkPackageParseEndTime;
}
