package com.shxp.apk.domain.po;

import lombok.Data;

import java.io.Serializable;


/**
 * app下载链接信息
 *
 * @author xinglei
 * @date 2021-02-24 18:20:06
 */
@Data
public class AppTelecomLink implements Serializable {

	/**
	 * 自增量，主键
	 * nullable : false
	 * default  : null
	 */
	private String id;

	/**
	 * app关联电信诈骗APP文件编号
	 * nullable : false
	 * default  : null
	 */
	private String appRelFileId;

	/**
	 * app对应文件名称
	 * nullable : false
	 * default  : null
	 */
	private String appFileName;

	/**
	 * app对应的下载的地址
	 * nullable : true
	 * default  : null
	 */
	private String appOriginLink;

	/**
	 * app下载链接对应在电信文件包的行号
	 * nullable : false
	 * default  : null
	 */
	private Long appOriginTextLine;

	/**
	 * 文件大小
	 * nullable : true
	 * default  : null
	 */
	private Long appFileSize;

	/**
	 * app下载时间
	 * nullable : true
	 * default  : null
	 */
	private String appDownloadTime;

	/**
	 * app是否下载（0：未下载，1：下载成功，-1：下载异常）
	 * nullable : true
	 * default  : 0
	 */
	private Integer appIsDown;

	/**
	 * app下载花费时间
	 * nullable : true
	 * default  : 0
	 */
	private Long appDownloadSpendTime;

	/**
	 * app静态分析对应的包名
	 * nullable : true
	 * default  : null
	 */
	private String appPackageName;

	/**
	 * app静态分析对应类名
	 * nullable : true
	 * default  : null
	 */
	private String appClassName;

	/**
	 * app静态分析应用名
	 * nullable : true
	 * default  : null
	 */
	private String appApplicationName;

	/**
	 * app静态分析对应版本号
	 * nullable : true
	 * default  : null
	 */
	private String appVersion;

	/**
	 * 增加时间
	 * nullable : true
	 * default  : CURRENT_TIMESTAMP
	 */
	private java.util.Date appAddTime;

	/**
	 * app更新时间
	 * nullable : true
	 * default  : null
	 */
	private java.util.Date appUpdateTime;


}
