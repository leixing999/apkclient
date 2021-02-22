package com.shxp.apk.task.service;

import com.shxp.apk.domain.po.ApkTelecomFileParsePo;

import java.util.List;

public interface ApkTelecomFileParseService {
    public  void  addApkTelecomFileParse(ApkTelecomFileParsePo apkTelecomFileParsePo);
    public List<ApkTelecomFileParsePo> getApkTelecomFileParseList(String apkFilename);

    public void updateApkTelecomFileParses(String id,String status);

    public  List<ApkTelecomFileParsePo> getApkTelecomFileParses(String analyseFlag);
}
