package com.github.soyanga.secondskillsystem.result;

import lombok.Getter;


/**
 * @program: seconds_kill_system
 * @Description: 请求返回封装
 * @Author: SOYANGA
 * @Create: 2019-05-14 19:33
 * @Version 1.0
 */
@Getter
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    /**
     * 成功结果返回
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(data);
    }

    /**
     * 异常结果返回
     * @param cm
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(CodeMsg cm) {
        return new Result<T>(cm);
    }

    private Result(T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }

    private Result(CodeMsg cm) {
        if (cm == null) {
            return;
        }
        this.code = cm.getCode();
        this.msg = cm.getMsg();
    }

}
