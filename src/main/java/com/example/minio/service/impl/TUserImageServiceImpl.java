package com.example.minio.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.minio.config.MinIOInfo;
import com.example.minio.dto.UserInfoDTO;
import com.example.minio.entity.TUserImage;
import com.example.minio.mapper.TUserImageMapper;
import com.example.minio.service.TUserImageService;
import com.example.minio.service.TUserInfoService;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import io.minio.http.Method;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2024-04-25
 */
@Service
public class TUserImageServiceImpl extends ServiceImpl<TUserImageMapper, TUserImage> implements TUserImageService {
    @Resource
    private MinioClient minioClient;
    @Resource
    private MinIOInfo minIOInfo;
    @Resource
    TUserInfoService userInfoService;

    @Override
    public void saveImage(MultipartFile file, Integer userId) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        String originalFilename = file.getOriginalFilename();
        System.out.println("test:" + originalFilename);
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        // 构建新的对象名，例如：用户ID_时间戳.扩展名，这里简单使用用户ID作为示例
        String objectName = userId + "-" + System.currentTimeMillis() + "." + extension;


        System.out.println("测试" + userId);
        String type = file.getContentType();
        System.out.println(type);

        minioClient.putObject(PutObjectArgs.builder()
                .bucket(minIOInfo.getBucket())
                //  .object("1.png")

                .object(objectName) // 使用动态生成的对象名
                .stream(file.getInputStream(), file.getSize(), -1) // 从文件流上传
                .contentType(file.getContentType()) // 添加正确的Content-Type
                .build());
        saveOrUpdateImagetoDb(userId, minIOInfo.getBucket(), objectName);
    }

    @Override
    public String downImage(Integer userId) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        UserInfoDTO userInfoDTO = userInfoService.selectUserById(12);

        String presignedObjectUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .bucket(userInfoDTO.getImageBucket())
                .object(userInfoDTO.getImageObject())
                .expiry(3, TimeUnit.MINUTES)
                .method(Method.GET)
                .build());
        System.out.println(presignedObjectUrl);
        return presignedObjectUrl;
    }

    private void saveOrUpdateImagetoDb(Integer userId, String bucket, String objectName) {
        LambdaQueryWrapper<TUserImage> wrapper = new LambdaQueryWrapper<>();


        wrapper.eq(TUserImage::getUid, userId);
        Long count = baseMapper.selectCount(wrapper);
        if (count > 0) {
            TUserImage userImage = new TUserImage();
            userImage.setBucket(bucket);
            userImage.setObject(objectName);
            baseMapper.update(userImage, wrapper);

        } else {
            TUserImage userImage = new TUserImage();
            userImage.setBucket(bucket);
            userImage.setObject(objectName);
            userImage.setUid(userId);
            baseMapper.insert(userImage);
        }

    }

}
