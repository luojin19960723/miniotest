package com.example.minio;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.example.minio.mapper")
@SpringBootApplication
public class MiniotestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniotestApplication.class, args);
    }

}
