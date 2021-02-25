package com.shxp.apk.task.service;

import com.shxp.apk.domain.po.AppTelecomLinkPackage;

import java.util.Date;

/*****
 * 将App客户端数据同步到服务器功能接口类
 * @author xinglei
 * @date 2021-02-25 15:27
 */
public interface ServerReqService {

     void syncAppTelecomLinkPackage(AppTelecomLinkPackage appTelecomLinkPackage);
     void updateLinkPackage(String id, Date EndTime, Integer currentLines, Integer status);
}
