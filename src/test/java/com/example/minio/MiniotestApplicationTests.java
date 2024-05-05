package com.example.minio;

import com.example.minio.config.MinIOInfo;
import com.example.minio.dto.UserInfoDTO;
import com.example.minio.mapper.TUserInfoMapper;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class MiniotestApplicationTests {
    @Resource
    TUserInfoMapper userInfoMapper;
    @Resource
    private MinIOInfo minIOInfo;


    @Test
    void contextLoads() {
        UserInfoDTO userInfoDTO = userInfoMapper.selectUserById(12);

        System.out.println(userInfoDTO);
    }


    @Resource
    private MinioClient minioClient;



    @Test
    void test01() throws Exception {
        boolean isBucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket("myfile").build());
        System.out.println("myfile目录是否存在：" + isBucketExists);
    }

    @Test
    void test02() throws Exception {
        String bucketName = "myfile2";
        boolean isBucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!isBucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        } else {
            System.out.println("bucket已经存在，不需要创建.");
        }

        String policyJsonString = "{\"Version\" : \"2012-10-17\",\"Statement\":[{\"Sid\":\"PublicRead\",\"Effect\":\"Allow\",\"Principal\":{\"AWS\":\"*\"},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::" + bucketName + "/*\"]}]}";
        //创建存储桶的时候，设置该存储桶里面的文件的访问策略，运行公开的读；
        minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                .bucket(bucketName)
                .config(policyJsonString)//json串，里面是访问策略
                .build());
    }

    @Test
    void test03() throws Exception {
        List<Bucket> bucketList = minioClient.listBuckets();
        bucketList.forEach(bucket -> {
            System.out.println(bucket.name() + " -- " + bucket.creationDate());
        });
    }

    @Test
    void test04() throws Exception {
        minioClient.removeBucket(RemoveBucketArgs.builder().bucket("myfile2").build());
    }

    //---------------------------------------------------------------------------

    @Test
    void test05() throws Exception {
        File file = new File("D:\\MinIO\\MinioClient.jpg");
        ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs.builder()
                .bucket("myfile")
                .object("test.jpg")
                .stream(new FileInputStream(file), file.length(), -1)
                .build()
        );
        System.out.println(objectWriteResponse);

        ObjectWriteResponse objectWriteResponse2 = minioClient.uploadObject(UploadObjectArgs.builder()
                .bucket("myfile")
                .object("test2.jpg")
                .filename("D:\\MinIO\\MinioClient.jpg")
                .build()
        );
        System.out.println(objectWriteResponse);

    }









    @Test
    void test06() throws Exception {
        StatObjectResponse statObjectResponse = minioClient.statObject(StatObjectArgs.builder()
                .bucket("myfile")
                .object("test.jpg")
                .build()
        );
        System.out.println(statObjectResponse);
    }

    @Test
    void test07() throws Exception {
        UserInfoDTO userInfoDTO = userInfoMapper.selectUserById(12);

        String presignedObjectUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .bucket(userInfoDTO.getImageBucket())
                .object(userInfoDTO.getImageObject())
                .expiry(3, TimeUnit.MINUTES)
                .method(Method.GET)
                .build());
        System.out.println(presignedObjectUrl);
    }

    @Test
    void test08() throws Exception {
        UserInfoDTO userInfoDTO = userInfoMapper.selectUserById(12);

        GetObjectResponse getObjectResponse = minioClient.getObject(GetObjectArgs.builder()
                .bucket(userInfoDTO.getImageBucket())
                .object(userInfoDTO.getImageObject())
                .build());

    minioClient.downloadObject(DownloadObjectArgs.builder()
                .bucket(userInfoDTO.getImageBucket())
                .object(userInfoDTO.getImageObject())
                .build());

        System.out.println(getObjectResponse.transferTo(null));
    }

    @Test
    void test09() throws Exception {
        Iterable<Result<Item>> listObjects = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket("myfile")
                .build());

        listObjects.forEach( itemResult -> {
            try {
                Item item = itemResult.get();
                System.out.println(item.objectName());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void test10() throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket("myfile")
                .object("test.jpg")
                .build());
    }
}
