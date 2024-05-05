package com.example.minio.config;

import io.minio.MinioClient;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinIOConfig {
    @Resource
    public MinIOInfo minIOInfo;
   @Bean
    public MinioClient minioClient(){
       return  MinioClient.builder().endpoint(minIOInfo.getEndpoint())
               .credentials(minIOInfo.getAccessKey(),minIOInfo.getSecretKey()).build();
   }
}
