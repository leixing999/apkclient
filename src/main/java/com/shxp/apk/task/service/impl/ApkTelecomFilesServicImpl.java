package com.shxp.apk.task.service.impl;

import com.shxp.apk.analyse.ApkInfo;
import com.shxp.apk.analyse.ApkUtil;
import com.shxp.apk.domain.po.*;
import com.shxp.apk.domain.vo.UrlPathVO;
import com.shxp.apk.task.MultiThreadDownload;
import com.shxp.apk.task.mapper.ApkTelecomFileMapper;
import com.shxp.apk.task.service.*;
import com.shxp.apk.utils.DateUtil;
import com.shxp.apk.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

@Service
public class ApkTelecomFilesServicImpl implements ApkTelecomFilesService {
    private static final Logger log = LoggerFactory.getLogger(ApkTelecomFilesServicImpl.class);

    @Autowired
    ApkTelecomFileMapper apkTelecomFileMapper;
    @Autowired
    PropertiesService propertiesService;
    @Autowired
    ApkTelecomFileParseService apkTelecomFileParseService;
    @Autowired
    ApkTelecomFileDetailService apkTelecomFileDetailService;
    @Autowired
    ServerReqService serverReqService;

    @Override
    public List<ApkTelecomFilesPo> getApkTelecomFiles() {
        return apkTelecomFileMapper.getApkTelecomFiles();
    }

    @Override
    public void addApkTelecomFiles(ApkTelecomFilesPo apkTelecomFilesPo) {
        apkTelecomFileMapper.addApkTelecomFiles(apkTelecomFilesPo);
    }

    @Override
    public List<ApkTelecomFilesPo> getApkTelecomFiles(ApkTelecomFilesPo apkTelecomFilesPo) {
        return apkTelecomFileMapper.getSingleApkTelecomFiles(apkTelecomFilesPo);
    }

    @Override
    public void updateApkTelecomFile(ApkTelecomFilesPo apkTelecomFilesPo) {
        apkTelecomFileMapper.updateApkTelecomFile(apkTelecomFilesPo);
    }

    /***
     * 扫描电信apk指定文件夹下的apk文件入库
     */
    @Override
    public void apkDealyService() {
        File files[] = FileUtils.search(propertiesService.getFilePath());

        for (File file : files) {
            long fileRecords = FileUtils.getContentByLineList(file.getPath()).size();
            ApkTelecomFilesPo apkTelecomFilesPo = new ApkTelecomFilesPo();
            apkTelecomFilesPo.setFileName(file.getName());
            if (this.getApkTelecomFiles(apkTelecomFilesPo).size() == 0) {
                apkTelecomFilesPo.setFilePath(file.getPath());
                apkTelecomFilesPo.setFileSize("" + file.length());
                apkTelecomFilesPo.setFileId(UUID.randomUUID().toString());
                apkTelecomFilesPo.setFileStatus("0");
                apkTelecomFilesPo.setFileFinishedRecords("0");
                apkTelecomFilesPo.setFileRecords(""+fileRecords);
                apkTelecomFilesPo.setFileImportTime(DateUtil.simpleDateFormatyMdHms());
                this.addApkTelecomFiles(apkTelecomFilesPo);

                AppTelecomLinkPackage appTelecomLinkPackage = new AppTelecomLinkPackage();
                appTelecomLinkPackage.setId(apkTelecomFilesPo.getFileId());
                appTelecomLinkPackage.setLinkPackageName(apkTelecomFilesPo.getFileName());
                appTelecomLinkPackage.setLinkPackageSize(file.length());
                appTelecomLinkPackage.setLinkPackageLines(fileRecords);
                appTelecomLinkPackage.setLinkPackageStatus(0);
                appTelecomLinkPackage.setLinkPackageAddTime(new Date());

                serverReqService.syncAppTelecomLinkPackage(appTelecomLinkPackage);

            } else {
                log.info("【apkDealyService】文件已存在，" + file.getName());
            }
        }
    }


    /****
     * 解析已经入库的电信apk文件里的具体apk下载信息
     */
    @Override
    public void apkParseService() {
        //获取待要下载的电信APK 文件信息
        List<ApkTelecomFilesPo> delayList = this.getApkTelecomFiles();
        //配置每次下载启动10个线程，每个线程下载对应的一个文件
        ExecutorService executor = Executors.newFixedThreadPool(10);
        //获取保存下载的文件路径
        String downloadFilePath = propertiesService.getDownloadpath();
        int maxThread = propertiesService.getMaxThread();
        //初始化解析电信APK文件的URL分析服务
        UrlPathService urlPathService = new UrlPathService();
        for (ApkTelecomFilesPo apkTelecomFilesPo : delayList) {

            //对电信APK文件下载进行归类为对应的URL 路径列表
            List<UrlPathVO> urlPathVOList = urlPathService.parseApkUrlPath(apkTelecomFilesPo.getFilePath());
            //apk现在线程队列
            List<ApkDownThread> myThreadList = null;
            //获取上次下载的电信APK包文件位置
            int nextDownloadIndex = Integer.parseInt(apkTelecomFilesPo.getFileFinishedRecords());
            //每次线程批量下载的开始索引
            int startIndex = 0;
            //每次线程批量下载的结束索引
            int endIndex = 0;
            //获取一批次下载的URL路径信息
            List<UrlPathVO> tempUrlPathVOList = null;
            //判断是否完成批量下载
            boolean isEnd = true;

            //向服务器同步开始下载状态
            serverReqService.updateLinkPackage(apkTelecomFilesPo.getFileId(), new Date(), nextDownloadIndex, null);
            while (isEnd) {
                startIndex = nextDownloadIndex * maxThread;
                endIndex = (nextDownloadIndex + 1) * maxThread;
                //获取本次队列要下载的APK路径队列
                if (endIndex < urlPathVOList.size()) {
                    tempUrlPathVOList = urlPathVOList.subList(startIndex, endIndex);
                } else {
                    tempUrlPathVOList = urlPathVOList.subList(startIndex, urlPathVOList.size()-1);
                    isEnd = false;
                }
                //初始化apk现在线程队列
                myThreadList = new ArrayList<>();
                UrlPathVO tempPathVo = null;
                for (int i = 0; i < tempUrlPathVOList.size(); i++) {
                    tempPathVo = tempUrlPathVOList.get(i);
                    //判断是否此APK是否下载过
                    if (apkTelecomFileParseService.getApkTelecomFileParseList(tempPathVo.getApkFileName()).size() == 0) {
                        boolean isExist = true;
                        //将待下载的APK信息添加到对应的队列中,判断是否有重复记录出现。
                        for(ApkDownThread apkDownThread : myThreadList){

                            if(tempPathVo.getApkFileName().equals(apkDownThread.getUrlPathVO().getApkFileName())){
                                isExist = false;
                                break;
                            }
                        }
                        if(isExist){
                            myThreadList.add(new ApkDownThread(tempPathVo, apkTelecomFilesPo.getFileId()));

                        }
                    }
                }

                try {
                    //获取本批次线程执行队列结果信息
                    List<Future<String>> futures = executor.invokeAll(myThreadList);
                    for (int i = 0; i < futures.size(); i++) {
                        log.info(futures.get(i).get());
                    }
                } catch (Exception e) {
                    ///System.out.println("下载异常："+e);
                    log.error("下载异常：" + e);

                } finally {
                    log.info("完成下载量===" + endIndex);
                    //执行将本次执行队列索引更新到数据库配置文件中
                    apkTelecomFilesPo.setFileFinishedRecords(nextDownloadIndex + "");
                    this.updateApkTelecomFile(apkTelecomFilesPo);
                }
                //获取下批次线程索引值
                nextDownloadIndex++;
                //向服务器同步apk下载完状态
                serverReqService.updateLinkPackage(apkTelecomFilesPo.getFileId(), null, nextDownloadIndex, null);

            }
            //完成本批次线程
            apkTelecomFilesPo.setFileStatus("2");
            apkTelecomFilesPo.setFileFinishedRecords(urlPathVOList.size() + "");
            this.updateApkTelecomFile(apkTelecomFilesPo);
            //向服务器同步apk全部下载完成状态
            serverReqService.updateLinkPackage(apkTelecomFilesPo.getFileId(), new Date(), nextDownloadIndex, 2);
        }
        executor.shutdown();

    }

    /***
     * Apk下载进程
     */
    class ApkDownThread implements Callable<String> {
        private UrlPathVO urlPathVO;
        private String fileId = null;

        public ApkDownThread(UrlPathVO urlPathVO, String fileId) {
            this.urlPathVO = urlPathVO;
            this.fileId = fileId;
        }
        public UrlPathVO getUrlPathVO(){
            return urlPathVO;
        }

        @Override
        public String call() throws Exception {
            String downloadFilePath = propertiesService.getDownloadpath();
            long fileMax = propertiesService.getFilemax();
            long startTime = System.currentTimeMillis();
            /**********启动下载过程**************/
            MultiThreadDownload mtd = new MultiThreadDownload(
                    urlPathVO.getRequestApkUrlPath(),
                    downloadFilePath + urlPathVO.getApkFileName(),
                    1);
            long fileSize = mtd.download(fileMax);
            boolean isLimit = mtd.getIsLimit();
            boolean isDown = mtd.getIsDown();
            /************结束下载过程******************/
            if (isDown || isLimit) {
                ApkTelecomFileParsePo apkTelecomFileParsePo = new ApkTelecomFileParsePo();
                apkTelecomFileParsePo.setFileId(this.fileId);
                apkTelecomFileParsePo.setApkFileName(urlPathVO.getApkFileName());
                apkTelecomFileParsePo.setId(UUID.randomUUID().toString());
                apkTelecomFileParsePo.setDecodeUrlPath(urlPathVO.getDecodeUrlPath());
                apkTelecomFileParsePo.setOriginUrlPath(urlPathVO.getOrignUrlPath());
                apkTelecomFileParsePo.setFileSize("" + fileSize);
                apkTelecomFileParsePo.setIsDown("true");

                long endTime = System.currentTimeMillis();
                apkTelecomFileParsePo.setDownloadTime("" + (endTime - startTime));
                //将下载的APK文件信息入库
                apkTelecomFileParseService.addApkTelecomFileParse(apkTelecomFileParsePo);

                AppTelecomLink appTelecomLink = new AppTelecomLink();
                appTelecomLink.setId(apkTelecomFileParsePo.getId());
                appTelecomLink.setAppRelFileId(this.fileId);
                appTelecomLink.setAppIsDown(2);
                appTelecomLink.setAppAddTime(new Date());
                appTelecomLink.setAppDownloadSpendTime((endTime - startTime));
                appTelecomLink.setAppOriginTextLine(1l);
                appTelecomLink.setAppOriginLink(urlPathVO.getRequestApkUrlPath());
                appTelecomLink.setAppFileName(apkTelecomFileParsePo.getApkFileName());
                appTelecomLink.setAppFileSize(fileSize);

                serverReqService.syncAppTelecomLink(appTelecomLink);

            }
            return "【" + urlPathVO.getApkFileName() + "】，下载完成！";
        }
    }


    @Override
    public void apkPareseDelayService(ApkTelecomFilesPo apkTelecomFilesPo) {


    }

    /****
     * 分析apk文件的的权限。包名以及主类等信息
     */
    @Override
    public void apkAnalyseService() {
        log.info("解析apk文件的的权限。包名以及主类等信息 开始");
        List<ApkTelecomFileParsePo> list = apkTelecomFileParseService.getApkTelecomFileParses("0");
        String status = "";

        for (ApkTelecomFileParsePo apkTelecomFileParsePo : list) {
            try {
                status = "2";
                String apkPath = propertiesService.getDownloadpath() + apkTelecomFileParsePo.getApkFileName();
                ApkInfo apkInfo = new ApkUtil().getApkInfo(apkPath);

                ApkTelecomFileDetailPo apkTelecomFileDetailPo = new ApkTelecomFileDetailPo();
                apkTelecomFileDetailPo.setId(UUID.randomUUID().toString());
                apkTelecomFileDetailPo.setApkFilename(apkTelecomFileParsePo.getApkFileName());
                apkTelecomFileDetailPo.setApkFilenameAlias(apkInfo.getApplicationLable());
                apkTelecomFileDetailPo.setApkClassName(apkInfo.getLaunchableActivity());
                apkTelecomFileDetailPo.setApkPackageName(apkInfo.getPackageName());
                apkTelecomFileDetailPo.setApkVersion(apkInfo.getVersionName());
                apkTelecomFileDetailPo.setApkPermissionName(apkInfo.getUsesPermissions().toString());
                apkTelecomFileDetailPo.setApkOriginName(apkInfo.toString());
                //将解析Apk信息插入Apk解包明细表中去
                apkTelecomFileDetailService.addApkFileDetail(apkTelecomFileDetailPo);


                AppTelecomLink appTelecomLink = new AppTelecomLink();
                appTelecomLink.setId(apkTelecomFileParsePo.getId());
                appTelecomLink.setAppAddTime(new Date());
                appTelecomLink.setAppClassName(apkInfo.getLaunchableActivity());
                appTelecomLink.setAppApplicationName(apkInfo.getApplicationLable());
                appTelecomLink.setAppVersion(apkInfo.getVersionName());
                appTelecomLink.setAppPackageName(apkInfo.getPackageName());
                appTelecomLink.setAppUpdateTime(new Date());

                //向服务器同步app对应静态解析信息
                serverReqService.updateAppTelecomLink(appTelecomLink);

                for(String permission : apkInfo.getUsesPermissions()){
                    try {
                        AppPermission appPermission = new AppPermission();
                        appPermission.setId(UUID.randomUUID().toString());
                        appPermission.setAppLinkId(appTelecomLink.getId());
                        appPermission.setAppPermissionName(permission);
                        serverReqService.syncAppPermission(appPermission);
                    }catch (Exception ex){
                        log.error("同步 app 权限失败！");
                    }

                }

//                appTelecomLink.setPermission(apkTelecomFileDetailPo.getApkPermissionName());

            } catch (Exception e) {
                status = "-1";
                log.error("apkAnalyseService 解析异常：" + e);
                e.printStackTrace();
            } finally {
                //更新apk解析表状态
                apkTelecomFileParseService.updateApkTelecomFileParses(apkTelecomFileParsePo.getId(), status);
            }
        }
        log.info("解析apk文件的的权限。包名以及主类等信息 结束");

    }
}
