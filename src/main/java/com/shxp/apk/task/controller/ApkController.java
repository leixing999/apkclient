package com.shxp.apk.task.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.shxp.apk.domain.po.ApkTelecomFilesPo;
import com.shxp.apk.task.mapper.ApkTelecomFileMapper;
import com.shxp.apk.task.service.ApkTelecomFileParseService;
import com.shxp.apk.task.service.ApkTelecomFilesService;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/apk")
public class ApkController {
    @Autowired
    ApkTelecomFilesService apkTelecomFilesService;
    @Autowired
    ApkTelecomFileParseService apkTelecomFileParseService;
    @RequestMapping("/list")
    public void list(){
        List<ApkTelecomFilesPo> list = apkTelecomFilesService.getApkTelecomFiles();
        System.out.println(list.size());
      //  return companyMapper.findList();
    }
    @RequestMapping("/add")
    public void add(){
        ApkTelecomFilesPo apkTelecomFilesPo = new ApkTelecomFilesPo();
        apkTelecomFilesPo.setFileId(UUID.randomUUID().toString());
        apkTelecomFilesPo.setFileName("apk.apk");
        apkTelecomFilesPo.setFilePath("g://");
        apkTelecomFilesPo.setFileFinishedRecords("10");
        apkTelecomFilesPo.setFileImportTime("2021-02-21 14:43:01");
        apkTelecomFilesPo.setFileSize("20000");
        apkTelecomFilesPo.setFileStatus("0");
        apkTelecomFilesPo.setFileRecords("10749");
        try {
            List<ApkTelecomFilesPo> list = apkTelecomFilesService.getApkTelecomFiles(apkTelecomFilesPo);
            if(list.size()==0){
                apkTelecomFilesService.addApkTelecomFiles(apkTelecomFilesPo);
            }else{
                System.out.println("exists");
            }
        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    @RequestMapping("/addApkParseService")
    public void addApkParseService(){
        apkTelecomFilesService.apkAnalyseService();
    }
}
