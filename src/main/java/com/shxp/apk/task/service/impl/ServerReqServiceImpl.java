package com.shxp.apk.task.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.shxp.apk.domain.po.AppTelecomLinkPackage;
import com.shxp.apk.task.service.ApkService;
import com.shxp.apk.task.service.PropertiesService;
import com.shxp.apk.task.service.ServerReqService;
import com.shxp.apk.utils.DateUtil;
import com.shxp.apk.utils.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ServerReqServiceImpl implements ServerReqService {
    private static final Logger log = LoggerFactory.getLogger(ServerReqServiceImpl.class);

    @Autowired
    PropertiesService propertiesService;
    /****
     * 同步电信解析文件信息
     * @param appTelecomLinkPackage
     */
    @Override
    public void syncAppTelecomLinkPackage(AppTelecomLinkPackage appTelecomLinkPackage) {
        JSONObject jsonObject = (JSONObject)JSONObject.toJSON(appTelecomLinkPackage);
        String appServerHttpUrl = propertiesService.getAppServerHttpUrl();
        try {
            String reqUrl = appServerHttpUrl;
            RestClient.postForm(reqUrl, jsonObject.toJSONString());
        }catch(Exception ex){
            log.error("error-->"+ex);
        }

    }

    /*****
     * 同步更新文件解析状态
     * @param id
     * @param time
     * @param currentLines
     * @param status
     */
    @Override
    public void updateLinkPackage(String id, Date time, Integer currentLines, Integer status) {

        String appServerHttpUrl = propertiesService.getAppServerHttpUrl();
        String urlParam ="";
        String reqUrl = "";
        if(time==null && status==null){
             urlParam = String.format("id=%s&currentLines=%s", id,currentLines);
             reqUrl = "/api/AppTelecomLinkPackage/updateLinkPackage";
        }
        if(time!=null && status==null){
            urlParam = String.format("id=%s&beginTime=s%&currentLines=%s", id, DateUtil.simpleDateFormatyMdHms(time),currentLines);
            reqUrl = "/api/AppTelecomLinkPackage/updateLinkPackageBeginTime";

        }
        if(time!=null && status!=null){
            urlParam = String.format("id=%s&beginTime=s%&currentLines=%s&status=%s", id,DateUtil.simpleDateFormatyMdHms(time),currentLines,status);
            reqUrl = "/api/AppTelecomLinkPackage/updateLinkPackageEndTime";

        }
        try {
            reqUrl = appServerHttpUrl+ reqUrl;
            RestClient.postForm(reqUrl, urlParam);
        }catch(Exception ex){
            log.error("error-->"+ex);
        }

    }
}
