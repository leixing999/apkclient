package com.shxp.apk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.shxp.apk.task.mapper")

public class ApkClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApkClientApplication.class, args);
    }

}
