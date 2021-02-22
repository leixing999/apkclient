package com.shxp.apk.task.service;

import com.shxp.apk.domain.po.ApkTelecomFilesPo;
import com.shxp.apk.domain.vo.UrlPathVO;
import com.shxp.apk.task.MultiThreadDownload;
import com.shxp.apk.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class ApkService {
    final static long MAX_FILE_SIZE =100*1024*1024;


    @Autowired
    ApkTelecomFilesService apkTelecomFilesService;

    public void runApkService(){
        System.out.println("------------扫描电信apk文件入库");
        apkTelecomFilesService.apkDealyService();
        System.out.println("------------解析电信apk文件入库");
        apkTelecomFilesService.apkParseService();;
    }
}
