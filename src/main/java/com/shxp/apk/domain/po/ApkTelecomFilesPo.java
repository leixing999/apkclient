package com.shxp.apk.domain.po;

/***
 * 电信文件对应的po
 * @author xinglei
 * @date 2021-02-21
 */
public class ApkTelecomFilesPo {
//    文件编号
    private String fileId;
//    文件名称
    private String fileName;
//    文件路径
    private String filePath;
//    文件大小
    private String fileSize;
//    文件记录数
    private String fileRecords;
//    文件完成数
    private String fileFinishedRecords;
//    文件导入时间
    private String fileImportTime;
//    文件状态
    private String fileStatus;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileRecords() {
        return fileRecords;
    }

    public void setFileRecords(String fileRecords) {
        this.fileRecords = fileRecords;
    }

    public String getFileFinishedRecords() {
        return fileFinishedRecords;
    }

    public void setFileFinishedRecords(String fileFinishedRecords) {
        this.fileFinishedRecords = fileFinishedRecords;
    }

    public String getFileImportTime() {
        return fileImportTime;
    }

    public void setFileImportTime(String fileImportTime) {
        this.fileImportTime = fileImportTime;
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }
}
