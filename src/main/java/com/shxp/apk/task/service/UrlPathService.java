package com.shxp.apk.task.service;

import com.shxp.apk.domain.vo.UrlPathVO;
import com.shxp.apk.utils.ConverPercent;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/****
 * apk url转换服务
 * @author xinglei
 * @date 2021-02-21
 */
@Service
public class UrlPathService {
    /***
     * 解析文本文件原始行信息转换成实际apk文件路径信息
     * @param urlTextList
     * @return urlPathVOList
     */
    public List<UrlPathVO> parseApkUrlPath(List<String> urlTextList){
        List<UrlPathVO> urlPathVOList = new ArrayList<>();
        for(String path : urlTextList){
            UrlPathVO pathVO = new UrlPathVO();
            String orignUrlPath = path;
            String decodeUrlPath = URLDecoderString(ConverPercent.convertPercent(orignUrlPath));
            boolean isApk = decodeUrlPath.lastIndexOf(".apk")>0 ? true: false;
            String requestUrlPath = isApk ? getRequestUrlPath(decodeUrlPath) : "";
            String apkFileName = isApk ? getApkFileName(requestUrlPath) : "";
            pathVO.setOrignUrlPath(orignUrlPath);
            pathVO.setDecodeUrlPath(decodeUrlPath);
            pathVO.setRequestApkUrlPath(requestUrlPath);
            pathVO.setApk(isApk);
//            pathVO.setApkFileName(new Random().nextInt(10000)+apkFileName);
            pathVO.setApkFileName(apkFileName);

            urlPathVOList.add(pathVO);
        }

        return urlPathVOList;
    }

    /***
     * 获取实际请求url路径
     * @param urlPathVOList
     * @return
     */
    public List<UrlPathVO> getActualApkUrlPath(List<UrlPathVO> urlPathVOList){
                List<UrlPathVO> actualApkUrlPathList = urlPathVOList.stream().filter(urlPathVO ->
                             urlPathVO.isApk() && urlPathVO.getApkFileName().length()>0
                  ).collect(Collectors.toList());
                return actualApkUrlPathList;
    }

    /***
     * 获取apk文件名称
     * @param url
     * @return
     */
    private  String getApkFileName(String url){
        String apkFileName = "";
        try {
            int apkStartPosition = url.lastIndexOf("/");
            int apkEndPosition = url.lastIndexOf(".apk");
            if(apkEndPosition>apkStartPosition){
                apkFileName = url.substring(apkStartPosition+1, apkEndPosition + 4);
            }
        }catch(Exception ex){
            System.out.println(ex);
        }
        return apkFileName.replace("?","");
    }

    /***
     * 获取真正读取apk的url路径
     * @param url
     * @return
     */
    private  String getRequestUrlPath(String url){
        String requestUrlPath = "";
        try {
            //获取apk文件起始位置
            int apkPosition = url.lastIndexOf(".apk");
            //获取http位置
            int httpPosition = url.lastIndexOf("https:") > 0 ? url.lastIndexOf("https:") : url.lastIndexOf("http:");
            if (httpPosition > 0 && httpPosition< apkPosition) {
                requestUrlPath = url.substring(httpPosition, apkPosition + 4);
            } else {
                requestUrlPath = url.substring(0, apkPosition + 4);
            }
        }catch(Exception ex){
            System.out.println(url);
        }
        return requestUrlPath;

    }

    /**
     * 对
     * @param str
     * @return
     */
    public  String URLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
