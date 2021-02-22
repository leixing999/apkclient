package com.shxp.apk.task.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
public class PropertiesService {

    @Value("${apkconfig.filepath}")
    String filePath;
    public String getFilePath(){
        return filePath;
    }

    @Value("${apkconfig.downloadpath}")
    String downloadpath;

    public String getDownloadpath(){
        return downloadpath;
    }

    @Value("${apkconfig.filemax}")
    long filemax;
    public long getFilemax(){
        return filemax;
    }
}
