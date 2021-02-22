package com.shxp.apk.task.mapper;

import com.shxp.apk.domain.po.ApkTelecomFileParsePo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    /****
     * 获取等待处理分析的apk信息列表
     * @param analyseFlag
     * @return
     */
    @Select("select id ,file_id fileId,apk_filename apkFilename from apk_telecom_files_parses where is_parse=#{analyseFlag}")
    public  List<ApkTelecomFileParsePo> getApkTelecomFileParses(String analyseFlag);

    /***
     * 更新apk信息表状态
     * @param id
     * @param status
     */
    @Update("update apk_telecom_files_parses Set is_parse=#{status} where id=#{id}")
    public void updateApkTelecomFileParses(String id,String status);
}
