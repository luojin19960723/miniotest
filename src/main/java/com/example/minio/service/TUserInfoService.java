package com.example.minio.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.minio.dto.UserInfoDTO;
import com.example.minio.entity.TUserInfo;
import io.minio.errors.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2024-04-25
 */
public interface TUserInfoService extends IService<TUserInfo> {
    public UserInfoDTO selectUserById( Integer id);

    void removeUserById(long id) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
}
