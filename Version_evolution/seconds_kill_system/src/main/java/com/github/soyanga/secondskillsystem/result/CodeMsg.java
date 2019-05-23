package com.github.soyanga.secondskillsystem.result;

import lombok.Getter;

/**
 * @program: seconds_kill_system
 * @Description: 错误代码+错误信息 专用错误信息处理
 * @Author: SOYANGA
 * @Create: 2019-05-14 20:03
 * @Version 1.0
 */
@Getter
public class CodeMsg {
    private int code;
    private String msg;

    /**
     * 通用异常
     */
    public static final CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static final CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    //登录模块 5002XX

    //商品模块 5003XX

    //订单模块 5004XX

    //秒杀模块 5005XX

    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
