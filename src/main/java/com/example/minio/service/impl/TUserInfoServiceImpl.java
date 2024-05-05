package com.example.minio.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.minio.dto.UserInfoDTO;
import com.example.minio.entity.TUserContract;
import com.example.minio.entity.TUserImage;
import com.example.minio.entity.TUserInfo;
import com.example.minio.mapper.TUserContractMapper;
import com.example.minio.mapper.TUserImageMapper;
import com.example.minio.mapper.TUserInfoMapper;
import com.example.minio.service.TUserInfoService;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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
public class TUserInfoServiceImpl extends ServiceImpl<TUserInfoMapper, TUserInfo> implements TUserInfoService {
    @Resource
    private TUserInfoMapper userInfoMapper;
    @Resource
    private TUserImageMapper userImageMapper;
    @Resource
    private TUserContractMapper contractMapper;
    @Resource
    private MinioClient minioClient;

    @Override
    public UserInfoDTO selectUserById(Integer id) {
        return userInfoMapper.selectUserById(id);
    }

    @Override
    public void removeUserById(long id) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        LambdaQueryWrapper<TUserImage> wrapperImage = new LambdaQueryWrapper<>();
        wrapperImage.eq(TUserImage::getUid, id);
        TUserImage userImage = userImageMapper.selectOne(wrapperImage);
        LambdaQueryWrapper<TUserContract> wrapperContract = new LambdaQueryWrapper<>();
        wrapperContract.eq(TUserContract::getUid, id);
        TUserContract contract = contractMapper.selectOne(wrapperContract);


        if (userImage != null) {
            minioClient.removeObject(RemoveObjectArgs.builder().
                    bucket(userImage.getBucket())
                    .object(userImage.getObject())
                    .build());
            userImageMapper.delete(wrapperImage);
        }
        if (contract != null) {
            minioClient.removeObject(RemoveObjectArgs.builder().
                    bucket(contract.getBucket())
                    .object(contract.getObject())
                    .build());
            contractMapper.delete(wrapperContract);
        }



        baseMapper.deleteById(id);

    }
}
