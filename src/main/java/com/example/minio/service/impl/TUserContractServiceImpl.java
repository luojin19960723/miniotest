package com.example.minio.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.minio.config.MinIOInfo;
import com.example.minio.entity.TUserContract;

import com.example.minio.mapper.TUserContractMapper;
import com.example.minio.service.TUserContractService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2024-04-25
 */
@Service
public class TUserContractServiceImpl extends ServiceImpl<TUserContractMapper, TUserContract> implements TUserContractService {
    @Resource
    private MinioClient minioClient;
    @Resource
    private MinIOInfo minIOInfo;

    @Override
    public void saveContract(MultipartFile file, Integer userId) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        String name = FilenameUtils.getBaseName(file.getOriginalFilename());
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String objectName = userId + name + "." + extension;

        minioClient.putObject(PutObjectArgs.builder()
                .bucket(minIOInfo.getBucket())
                .object(objectName)
                .contentType(file.getContentType())
                .stream(file.getInputStream(), file.getSize(), -1).build());

          saveOrUpdateContractToDb(userId,minIOInfo.getBucket(),objectName);
    }

    private void saveOrUpdateContractToDb(Integer userId, String bucket, String objectName) {
        LambdaQueryWrapper<TUserContract> wrapper = new LambdaQueryWrapper<>();
        TUserContract userContract = new TUserContract();
        userContract.setBucket(bucket);
        userContract.setObject(objectName);
        userContract.setUid(userId);
        wrapper.eq(TUserContract::getUid, userId);
        Long count = baseMapper.selectCount(wrapper);
        if (count > 0) {

            baseMapper.updateById(userContract);

        } else {
            baseMapper.insert(userContract);
        }

    }
}
