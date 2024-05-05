package com.example.minio.mapper;

import com.example.minio.dto.UserInfoDTO;
import com.example.minio.entity.TUserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2024-04-25
 */
public interface TUserInfoMapper extends BaseMapper<TUserInfo> {
    public UserInfoDTO selectUserById(@Param("id") Integer id);


}
