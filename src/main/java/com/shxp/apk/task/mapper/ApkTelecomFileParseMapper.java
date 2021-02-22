package com.shxp.apk.task.mapper;

import com.shxp.apk.domain.po.ApkTelecomFileParsePo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ApkTelecomFileParseMapper {

    /****
     * 增加符合条件的apk信息入库
     * @param apkTelecomFileParsePo
     */
    @Insert("insert into apk_telecom_files_parses(" +
            "id,file_id,origin_url_path,decode_url_path," +
            "apk_filename,file_size,is_down,download_time) " +
            "values(" +
            "#{id},#{fileId},#{originUrlPath},#{decodeUrlPath}," +
            "#{apkFileName},#{fileSize},#{isDown},#{downloadTime}" +
            ")")
    public void addApkTelecomFileParse(ApkTelecomFileParsePo apkTelecomFileParsePo);

    /****
     * 获取指定文件是否已经下载
     * @param apkFilename
     * @return
     */
    @Select("select * from apk_telecom_files_parses where apk_filename=#{apkFilename}")
    public List<ApkTelecomFileParsePo> getApkTelecomFileParseList(@Param("apkFilename") String apkFilename);
}
