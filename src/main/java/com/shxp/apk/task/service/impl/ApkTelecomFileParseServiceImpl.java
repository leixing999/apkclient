package com.shxp.apk.task.service.impl;

import com.shxp.apk.domain.po.ApkTelecomFileParsePo;
import com.shxp.apk.task.mapper.ApkTelecomFileParseMapper;
import com.shxp.apk.task.service.ApkTelecomFileParseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApkTelecomFileParseServiceImpl implements ApkTelecomFileParseService {
    @Autowired
    ApkTelecomFileParseMapper apkTelecomFileParseMapper;

    @Override
    public synchronized void addApkTelecomFileParse(ApkTelecomFileParsePo apkTelecomFileParsePo) {
        apkTelecomFileParseMapper.addApkTelecomFileParse(apkTelecomFileParsePo);

    }

    @Override
    public List<ApkTelecomFileParsePo> getApkTelecomFileParseList(String apkFilename) {
        return apkTelecomFileParseMapper.getApkTelecomFileParseList(apkFilename);
    }

    @Override
    public void updateApkTelecomFileParses(String id, String status) {
        apkTelecomFileParseMapper.updateApkTelecomFileParses(id,status);
    }

    @Override
    public List<ApkTelecomFileParsePo> getApkTelecomFileParses(String analyseFlag) {
        return apkTelecomFileParseMapper.getApkTelecomFileParses(analyseFlag);
    }
}
