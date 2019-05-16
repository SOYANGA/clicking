package com.github.soyanga.secondskillsystem.exception;

import com.github.soyanga.secondskillsystem.result.CodeMsg;
import com.github.soyanga.secondskillsystem.result.Result;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.BindException;
import java.util.List;

/**
 * @program: seconds_kill_system _2
 * @Description: 讲异常统一处理 全局异常处理器
 * @Author: SOYANGA
 * @Create: 2019-05-16 16:42
 * @Version 1.0
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)  //该注解声明异常处理方法
    public Result<String> exceptionHandler(HttpServletRequest request, Exception e) {
        //对于自定义异常的处理 处理登陆异常错误处理
        if (e instanceof GlobalException) {
            GlobalException globalException = (GlobalException) e;
            return Result.error(globalException.getCodeMsg());
        } else if (e instanceof org.springframework.validation.BindException) {//对于绑定异常的处理，使用jsr303中的自定义注解抛出的异常属于绑定异常
            org.springframework.validation.BindException bindException = (org.springframework.validation.BindException) e;
            List<ObjectError> errors = bindException.getAllErrors();
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        } else {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
