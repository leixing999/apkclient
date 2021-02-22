package com.shxp.apk.task;


import com.shxp.apk.domain.vo.UrlPathVO;
import com.shxp.apk.task.service.UrlPathService;
import com.shxp.apk.utils.ConverPercent;
import com.shxp.apk.utils.FileUtils;
import com.sun.org.apache.xpath.internal.objects.XString;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Test {
    final static long MAX_FILE_SIZE =100*1024*1024;
    public static String URLDecoderString(String str) {
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
     public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
         long time1 = System.currentTimeMillis();
         String [] fileList ={
                 "http://appdownload.alicdn.com/publish/litetao_android/latest/litetao_android_1568972753113.apk",
                 "http://appdownload.alicdn.com/publish/litetao_android/latest/litetao_android_1591010737397.apk",
                 "https://toufang.coohua.com/app/xfct/csjxfctyf0102/release.apk",
                 "http://acj6.0098118.com/pc6_soure/2020-7-18/c72cfe26cadad28qc0qev7nmXGnDTk.apk",
                 "http://acj6.0098118.com/pc6_soure/2020-9-5/3e590d9c3312fa1SEdRGXkPO9tPq5M.apk",
                 "http://a7.0098118.com/yll6/liufangzhiluhz.apk",
                 "http://a7.0098118.com/zy6/Richman4fun.apk",
                 "http://a7.pc6.com/lzy6/maliaopaokurun.apk",
                 "http://aa6.0098118.com/pc6_soure/2016-6/net.ideasource.ntt_28.apk",
                 "http://aa9.0098118.com/lwl8/kekegongzhudechulain.apk",
                 "http://acj22.0098118.com/pc6_soure/2017-3/com.tencent.gamehelper_17030202.apk",
                 "http://acj3.0098118.com/pc6_soure/2018-3/com.lzh222333.olxzxs.unzip_173.apk",
                 "http://a.xzfile.com/anzhuo/yangshiyingyinhdv6.8.8_downcc.com.apk",
                 "http://a.zongyihui.cn:30300/filestore/apks/hndzgp_android_v3.0.0.apk",
                 "http://a6.pc6.com/qsj5/oppoyouxizhongxin.apk"
         };


         long startTime = System.currentTimeMillis();
         List<String> list = FileUtils.getContentByLineList("G://apk.txt");
         UrlPathService urlPathService = new UrlPathService();
         List<UrlPathVO> urlPathVOList = urlPathService.parseApkUrlPath(list);

         for(int i=0;i<urlPathVOList.size();i++){
             UrlPathVO urlPathVO = urlPathVOList.get(i);
             MultiThreadDownload mtd = new MultiThreadDownload(urlPathVO.getRequestApkUrlPath(), "G:\\"+urlPathVO.getApkFileName(), 6);
             long fileSize = mtd.download(MAX_FILE_SIZE);
             System.out.println(fileSize);
             System.out.println(mtd.getFailThreadNum());
             System.out.println("---------------------------------------i="+i);
         }


     }

}
