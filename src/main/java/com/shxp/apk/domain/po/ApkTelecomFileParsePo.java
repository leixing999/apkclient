package com.shxp.apk.domain.po;

public class ApkTelecomFileParsePo {
    private String id;
    private String fileId;
    private String originUrlPath;
    private String decodeUrlPath;
    private String apkFileName;
    private String fileSize;
    private String isDown;
    private String downloadTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getOriginUrlPath() {
        return originUrlPath;
    }

    public void setOriginUrlPath(String originUrlPath) {
        this.originUrlPath = originUrlPath;
    }

    public String getDecodeUrlPath() {
        return decodeUrlPath;
    }

    public void setDecodeUrlPath(String decodeUrlPath) {
        this.decodeUrlPath = decodeUrlPath;
    }

    public String getApkFileName() {
        return apkFileName;
    }

    public void setApkFileName(String apkFileName) {
        this.apkFileName = apkFileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getIsDown() {
        return isDown;
    }

    public void setIsDown(String isDown) {
        this.isDown = isDown;
    }

    public String getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(String downloadTime) {
        this.downloadTime = downloadTime;
    }
}
