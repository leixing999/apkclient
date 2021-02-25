package com.shxp.apk.utils;

import org.springframework.http.*;

public class RestClient {
    public static String postJson(String reqUrl, String reqJsonStrParam) {
        //设置 Header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        //设置参数
        HttpEntity<String> requestEntity = new HttpEntity<>(reqJsonStrParam, httpHeaders);
        //执行请求
        ResponseEntity<String> resp = RestEnum.SINGLE_INSTANCE.getRestTemplate()
                .exchange(reqUrl, HttpMethod.POST, requestEntity, String.class);
        //返回请求返回值
        return resp.getBody();
    }
    public static String postForm(String reqUrl, String reqFormPara) {
        //设置 Header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //设置参数
        HttpEntity<String> requestEntity = new HttpEntity<>(reqFormPara, httpHeaders);
        //执行请求
        ResponseEntity<String> resp = RestEnum.SINGLE_INSTANCE.getRestTemplate()
                .exchange(reqUrl, HttpMethod.POST, requestEntity, String.class);
        //返回请求返回值
        return resp.getBody();
    }

    public static void main(String[] args) {
        String id ="1111";
        String currentLines="10000";
       String urlParam = String.format("id=%s&currentLines=%s", id,currentLines);
       String  reqUrl = "/api/AppTelecomLinkPackage/updateLinkPackage";
        System.out.println(urlParam);
    }
}
