package com.example.minio.controller;


import com.example.minio.service.TUserImageService;
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
@CrossOrigin
@RestController
@RequestMapping("/image")
public class TUserImageController {

    @Resource
    private TUserImageService imageService;
    @PostMapping("/save")
    public Result image(MultipartFile file, @RequestParam("userId") Integer userId) throws Exception {
        imageService.saveImage(file,userId);
        return Result.ok();
    }
@GetMapping ("/down/{userId}")
    public Result imageDown(@PathVariable("userId") Integer userId) throws Exception {
    String presignedObjectUrl    =imageService.downImage(userId);

        return Result.ok(presignedObjectUrl);
    }

}

