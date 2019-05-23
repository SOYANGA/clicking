package com.github.soyanga.secondskillsystem.exception;

import com.github.soyanga.secondskillsystem.result.CodeMsg;

/**
 * @program: seconds_kill_system _2
 * @Description: 定义全局异常类
 * @Author: SOYANGA
 * @Create: 2019-05-16 16:57
 * @Version 1.0
 */
public class GlobalException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private CodeMsg codeMsg;

    public GlobalException(CodeMsg codeMsg) {
        super(codeMsg.toString());
        this.codeMsg = codeMsg;
    }

    public CodeMsg getCodeMsg() {
        return codeMsg;
    }
}
