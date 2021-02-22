package com.shxp.apk.task.mapper;

import com.shxp.apk.domain.po.ApkTelecomFileDetailPo;
import org.apache.ibatis.annotations.Insert;

public interface ApkTelecomFileDetailMapper {

    @Insert("insert into " +
            "apk_telecom_files_details(" +
            "id,apk_filename,apk_package_name,apk_filename_alias," +
            "apk_class_name,apk_permission_name,apk_origin_name,apk_version) values(" +
            "#{id},#{apkFilename},#{apkPackageName},#{apkFilenameAlias}," +
            "#{apkClassName},#{apkPermissionName},#{apkOriginName},#{apkVersion})")
    void addApkFileDetail(ApkTelecomFileDetailPo apkTelecomFileDetailPo);
}
