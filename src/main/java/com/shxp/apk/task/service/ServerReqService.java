package com.shxp.apk.task.service;

import com.shxp.apk.domain.po.AppPermission;
import com.shxp.apk.domain.po.AppTelecomLink;
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
     /****
      * 同步电信解析文件信息
      * @param appTelecomLink
      */
     public void syncAppTelecomLink(AppTelecomLink appTelecomLink);

     /****
      * 同步电信解析文件信息
      * @param appTelecomLink
      */
     public void updateAppTelecomLink(AppTelecomLink appTelecomLink);

     /****
      * 同步电信解析app权限信息
      * @param appPermission
      */
     public void syncAppPermission(AppPermission appPermission);
}
