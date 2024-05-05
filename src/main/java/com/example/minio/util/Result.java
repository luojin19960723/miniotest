package com.example.minio.util;

import lombok.Data;

@Data
public class Result <T>{


    private Integer code;
    private String message;
    private T data;

    private static <T> Result bulid(T data, String message, Integer code) {
        Result<T> result = new Result<>();
        if (data != null) {
            result.setData(data);
        }
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static <T> Result ok(T data) {
        return bulid(data, "成功", 200);
    }

    public static <T> Result ok() {
        return Result.ok(null);
    }
    public static <T> Result fail(T data) {
        return bulid(data, "失败", 201);
    } public static <T> Result fail() {
        return Result.fail(null);
    }
}
