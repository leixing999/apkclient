package com.shxp.apk.task.service;

import com.shxp.apk.domain.po.ApkTelecomFilesPo;

import java.util.List;

public interface ApkTelecomFilesService {
    public List<ApkTelecomFilesPo> getApkTelecomFiles();
    public void addApkTelecomFiles(ApkTelecomFilesPo apkTelecomFilesPo);
    public List<ApkTelecomFilesPo> getApkTelecomFiles(ApkTelecomFilesPo apkTelecomFilesPo);
    public void updateApkTelecomFile(ApkTelecomFilesPo apkTelecomFilesPo);

    public void apkDealyService();

    public void apkParseService();

    public void apkPareseDelayService(ApkTelecomFilesPo apkTelecomFilesPo);
}
