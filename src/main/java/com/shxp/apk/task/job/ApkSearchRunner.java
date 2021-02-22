package com.shxp.apk.task.job;

import com.shxp.apk.task.service.ApkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
/***
 * 启动默认扫描电信apk文件扫描和解析服务
 * @author xinglei
 * @date 2021-02-21
 */
@Component
@Order(1)
public class ApkSearchRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(ApkSearchRunner.class);
    @Autowired
    ApkService apkService;
    public void run(ApplicationArguments args) throws Exception {
        log.info("启动默认扫描电信apk文件扫描和解析服务");
        apkService.runApkService();
        log.info("结束默认扫描电信apk文件扫描和解析服务");
    }
}
