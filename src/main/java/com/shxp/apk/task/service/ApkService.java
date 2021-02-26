package com.shxp.apk.task.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ApkService {
    final static long MAX_FILE_SIZE =100*1024*1024;
    private static final Logger log = LoggerFactory.getLogger(ApkService.class);



    @Autowired
    ApkTelecomFilesService apkTelecomFilesService;

    public void runApkService()   {
        while(true) {
            try {
                log.info("------------扫描电信apk文件入库");
                apkTelecomFilesService.apkDealyService();
                log.info("------------解析电信apk文件入库");
                apkTelecomFilesService.apkParseService();

                log.info("------------静态分析电信app");

                apkTelecomFilesService.apkAnalyseService();
                Thread.sleep(10000);
            }catch(InterruptedException ex){
                log.info("runApkService"+ex);
            }
        }
    }
}
