package com.shxp.apk.task.service.impl;

import com.shxp.apk.domain.po.ApkTelecomFileParsePo;
import com.shxp.apk.domain.po.ApkTelecomFilesPo;
import com.shxp.apk.domain.vo.UrlPathVO;
import com.shxp.apk.task.MultiThreadDownload;
import com.shxp.apk.task.mapper.ApkTelecomFileMapper;
import com.shxp.apk.task.service.*;
import com.shxp.apk.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

@Service
public class ApkTelecomFilesServicImpl implements ApkTelecomFilesService {
    @Autowired
    ApkTelecomFileMapper apkTelecomFileMapper;
    @Autowired
    PropertiesService propertiesService;
    @Autowired
    ApkTelecomFileParseService apkTelecomFileParseService;

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
            String fileRecords = "" + FileUtils.getContentByLineList(file.getPath()).size();
            ApkTelecomFilesPo apkTelecomFilesPo = new ApkTelecomFilesPo();
            apkTelecomFilesPo.setFileName(file.getName());
            if (this.getApkTelecomFiles(apkTelecomFilesPo).size() == 0) {
                apkTelecomFilesPo.setFilePath(file.getPath());
                apkTelecomFilesPo.setFileSize("" + file.length());
                apkTelecomFilesPo.setFileId(UUID.randomUUID().toString());
                apkTelecomFilesPo.setFileStatus("0");
                apkTelecomFilesPo.setFileFinishedRecords("");
                apkTelecomFilesPo.setFileRecords(fileRecords);
                apkTelecomFilesPo.setFileImportTime("");
                this.addApkTelecomFiles(apkTelecomFilesPo);
            } else {
                System.out.println("exists");
            }
        }
    }

    /****
     * 解析已经入库的电信apk文件里的具体apk下载信息
     */
    @Override
    public void apkParseService() {
        List<ApkTelecomFilesPo> delayList = this.getApkTelecomFiles();
        ExecutorService executor =Executors.newFixedThreadPool(10);
        String downloadFilePath = propertiesService.getDownloadpath();
        UrlPathService urlPathService = new UrlPathService();
        for (ApkTelecomFilesPo apkTelecomFilesPo : delayList) {

            List<String> list = FileUtils.getContentByLineList(apkTelecomFilesPo.getFilePath());
            List<UrlPathVO> urlPathVOList = urlPathService.parseApkUrlPath(list);

            List<ApkDownThread> myThreadList = null;
            int j =Integer.parseInt(apkTelecomFilesPo.getFileFinishedRecords());
            int startIndex =0;
            int endIndex = 0 ;
            List<UrlPathVO> tempUrlPathVOList = null;
            boolean isEnd = true;
            while(isEnd){
                startIndex = j*10;
                endIndex = (j+1)*10;
                if(endIndex < urlPathVOList.size()){
                     tempUrlPathVOList =urlPathVOList.subList(startIndex,endIndex);
                }else{
                    tempUrlPathVOList =urlPathVOList.subList(startIndex,urlPathVOList.size());
                    isEnd = false;
                }
                myThreadList = new ArrayList<>();
                UrlPathVO tempPathVo = null;
                for(int i=0;i<10;i++){
                    tempPathVo = tempUrlPathVOList.get(i);
                    if (apkTelecomFileParseService.getApkTelecomFileParseList(tempPathVo.getApkFileName()).size() == 0) {
                        myThreadList.add(new ApkDownThread(tempPathVo,apkTelecomFilesPo.getFileId()));
                    }
                }

                try {
                    List<Future<String>> futures =  executor.invokeAll(myThreadList);
                    for(int i=0;i<futures.size();i++){
                        System.out.println(futures.get(i).get());
                    }

                } catch (Exception e) {
                    System.out.println("下载异常："+e);

                } finally {
                 //   executor.shutdown();
                    System.out.println("完成下载量==="+endIndex);
                    apkTelecomFilesPo.setFileFinishedRecords(j+"");
                    this.updateApkTelecomFile(apkTelecomFilesPo);
                }
                j++;


            }
            apkTelecomFilesPo.setFileStatus("2");
            apkTelecomFilesPo.setFileFinishedRecords(urlPathVOList.size()+"");
            this.updateApkTelecomFile(apkTelecomFilesPo);
        }
        executor.shutdown();

    }

    /***
     * Apk下载进程
     */
     class ApkDownThread implements Callable<String> {
        private UrlPathVO urlPathVO;
        private String fileId =null;
        public ApkDownThread(UrlPathVO urlPathVO,String fileId){
            this.urlPathVO = urlPathVO;
            this.fileId = fileId;
        }
        @Override
        public String call() throws Exception {
            String downloadFilePath = propertiesService.getDownloadpath();
            long fileMax = propertiesService.getFilemax();
            long startTime = System.currentTimeMillis();
            MultiThreadDownload mtd = new MultiThreadDownload(
                    urlPathVO.getRequestApkUrlPath(),
                    downloadFilePath + urlPathVO.getApkFileName(),
                    1);
            long fileSize = mtd.download(fileMax);
            boolean isLimit = mtd.getIsLimit();
            boolean isDown = mtd.getIsDown();
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
                apkTelecomFileParseService.addApkTelecomFileParse(apkTelecomFileParsePo);

            }
            return "【"+urlPathVO.getApkFileName() +"】，下载完成！";
        }
    }



    @Override
    public void apkPareseDelayService(ApkTelecomFilesPo apkTelecomFilesPo) {

        long fileMax = propertiesService.getFilemax();
        String downloadFilePath = propertiesService.getDownloadpath();
        int finishedRecords = Integer.parseInt(apkTelecomFilesPo.getFileFinishedRecords());
        List<String> list = FileUtils.getContentByLineList(apkTelecomFilesPo.getFilePath());

        UrlPathService urlPathService = new UrlPathService();
        List<UrlPathVO> urlPathVOList = urlPathService.parseApkUrlPath(list);

        for (int i = finishedRecords; i < urlPathVOList.size(); i++) {
            try {
                UrlPathVO urlPathVO = urlPathVOList.get(i);
                if (apkTelecomFileParseService.getApkTelecomFileParseList(urlPathVO.getApkFileName()).size() == 0) {
                    long startTime = System.currentTimeMillis();
                    MultiThreadDownload mtd = new MultiThreadDownload(
                            urlPathVO.getRequestApkUrlPath(),
                            downloadFilePath + urlPathVO.getApkFileName(),
                            1);
                    long fileSize = mtd.download(fileMax);
                    int failThreadNum = mtd.getFailThreadNum();
                    boolean isDown = mtd.getIsDown();
                    boolean isLimit = mtd.getIsLimit();
                    if (isDown || isLimit) {
                        ApkTelecomFileParsePo apkTelecomFileParsePo = new ApkTelecomFileParsePo();
                        apkTelecomFileParsePo.setFileId(apkTelecomFilesPo.getFileId());
                        apkTelecomFileParsePo.setApkFileName(urlPathVO.getApkFileName());
                        apkTelecomFileParsePo.setId(UUID.randomUUID().toString());
                        apkTelecomFileParsePo.setDecodeUrlPath(urlPathVO.getDecodeUrlPath());
                        apkTelecomFileParsePo.setOriginUrlPath(urlPathVO.getOrignUrlPath());
                        apkTelecomFileParsePo.setFileSize("" + fileSize);
                        apkTelecomFileParsePo.setIsDown("true");

                        long endTime = System.currentTimeMillis();
                        apkTelecomFileParsePo.setDownloadTime("" + (endTime - startTime));
                        apkTelecomFileParseService.addApkTelecomFileParse(apkTelecomFileParsePo);

                    }
                    System.out.println("---------------------------------------i=" + i);
                }

            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }
}
