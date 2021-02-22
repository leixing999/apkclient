package com.shxp.apk.task.mapper;

import com.shxp.apk.domain.po.ApkTelecomFilesPo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ApkTelecomFileMapper {

    /***
     * 获取待解析电信apk文件信息
     * @return
     */
    @Select("select file_id fileId,\n" +
            "       file_name fileName,\n" +
            "       file_path filePath,\n" +
            "       file_size fileSize,\n" +
            "       file_records fileRecords,\n" +
            "       file_finished_records fileFinishedRecords,\n" +
            "       file_import_time fileImportTime,\n" +
            "       file_status fileStatus from apk_telecom_files where file_status='0'")
    public List<ApkTelecomFilesPo> getApkTelecomFiles();

    /***
     * 新增电信文件解析文件包
     * @param apkTelecomFilesPo
     */
    @Insert("insert into apk_telecom_files(" +
                 "file_id,file_name,file_path,file_size," +
                 "file_records,file_finished_records," +
                 "file_import_time,file_status" +
            ") values(" +
                  "#{fileId},#{fileName},#{filePath},#{fileSize}," +
                  "#{fileRecords},#{fileFinishedRecords},#{fileImportTime}," +
                  "#{fileStatus}" +
            ") ")
    public void addApkTelecomFiles(ApkTelecomFilesPo apkTelecomFilesPo);

    /***
     * 获取指定电信apk包
     * @param apkTelecomFilesPo
     * @return
     */
    @Select("select * from apk_telecom_files where file_name=#{fileName}")
    public List<ApkTelecomFilesPo> getSingleApkTelecomFiles(ApkTelecomFilesPo apkTelecomFilesPo);

    /***
     * 修改文件信息
     * @param apkTelecomFilesPo
     */
    @Update("update apk_telecom_files " +
            " set file_Finished_Records=#{fileFinishedRecords},file_Status=#{fileStatus}" +
            " where file_id=#{fileId}")
    public void updateApkTelecomFile(ApkTelecomFilesPo apkTelecomFilesPo);

}
