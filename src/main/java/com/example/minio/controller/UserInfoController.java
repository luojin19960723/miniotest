package com.example.minio.controller;


import com.example.minio.config.MinIOInfo;
import com.example.minio.dto.UserInfoDTO;
import com.example.minio.entity.TUserInfo;
import com.example.minio.service.TUserImageService;
import com.example.minio.service.TUserInfoService;
import com.example.minio.util.Result;
import io.minio.MinioClient;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2024-04-25
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserInfoController {
    @Resource
    public MinIOInfo minIOInfo;

    @Resource
    public TUserInfoService userInfoService;
    @Resource
    public MinioClient minioClient;
    @Resource
    private TUserImageService imageService;


    @GetMapping("/getAll")
    public Result getAll() {
        return Result.ok(userInfoService.list());
    }

    @GetMapping("/get/{id}")
    public Result getById(@PathVariable("id") Integer id) {
        return Result.ok(userInfoService.selectUserById(id));
    }

    @DeleteMapping("/remove/{id}")
    public Result removeById(@PathVariable("id") long id) {
        try {
            userInfoService.removeUserById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Result.ok();
    }


    @PutMapping("/update")
    public Result updateById(@RequestBody UserInfoDTO userInfoDTO) {
        TUserInfo userInfo = new TUserInfo();
        BeanUtils.copyProperties(userInfoDTO,userInfo);
        System.out.println("测试"+userInfo);
        return Result.ok(userInfoService.updateById(userInfo));
    }



}

