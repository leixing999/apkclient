package com.shxp.apk.task.service.impl;

import com.shxp.apk.domain.po.ApkTelecomFileDetailPo;
import com.shxp.apk.task.mapper.ApkTelecomFileDetailMapper;
import com.shxp.apk.task.service.ApkTelecomFileDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApkTelecomFileDetailServiceImpl implements ApkTelecomFileDetailService {
    private static final Logger log = LoggerFactory.getLogger(ApkTelecomFileDetailServiceImpl.class);

    @Autowired
    ApkTelecomFileDetailMapper apkTelecomFileDetailMapper;
    @Override
    public void addApkFileDetail(ApkTelecomFileDetailPo apkTelecomFileDetailPo) {
        try{
            apkTelecomFileDetailMapper.addApkFileDetail(apkTelecomFileDetailPo);
        }catch(Exception ex){
            log.error("addApkFileDetail:"+ex);
        }
    }
}
