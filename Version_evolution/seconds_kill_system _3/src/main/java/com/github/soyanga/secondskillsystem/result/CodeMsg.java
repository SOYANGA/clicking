package com.github.soyanga.secondskillsystem.result;

import lombok.Getter;
import sun.security.provider.SHA;

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
    public static final CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常:%s");
    //登录模块 5002XX
    public static final CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "密码不能为空");
    public static final CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "手机号码不能为空");
    public static final CodeMsg MOBILE_ERROR = new CodeMsg(500213, "手机号码格式错误");
    public static final CodeMsg MOBILE_NOT_EXIT = new CodeMsg(500214, "手机号码不存在");
    public static final CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "密码错误");

    //商品模块 5003XX

    //订单模块 5004XX

    //秒杀模块 5005XX
    public static final CodeMsg MIAO_SHA_OVER = new CodeMsg(500500, "商品已将秒杀完毕");
    public static final CodeMsg REPEATE_MIAOSHA = new CodeMsg(500501, "不能重复秒杀");


    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 返回带参数的错误码
     * eg:绑定异常
     *
     * @param args
     * @return
     */
    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }
}
