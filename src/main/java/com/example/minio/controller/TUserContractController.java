package com.example.minio.controller;


import com.example.minio.service.TUserContractService;
import com.example.minio.util.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2024-04-25
 */
@RestController
@CrossOrigin
@RequestMapping("/contract")
public class TUserContractController {
    @Resource
    public TUserContractService userContractService;

    @PostMapping("/save")
    public Result constract(MultipartFile file, @RequestParam("userId") Integer userId) throws Exception {
        userContractService.saveContract(file,userId);

        return Result.ok();


    }
}

