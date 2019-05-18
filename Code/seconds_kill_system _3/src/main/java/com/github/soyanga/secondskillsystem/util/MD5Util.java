package com.github.soyanga.secondskillsystem.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @program: seconds_kill_system _2
 * @Description: MD5两次加密 明文密码两次MD5处理
 * @Author: SOYANGA
 * @Create: 2019-05-16 11:00
 * @Version 1.0
 */
public class MD5Util {

    /**
     * 用户输入-->表单提交的固定盐
     */
    private static final String SALT = "1a2b3c4d";

    /**
     * MD5加密算法封装
     *
     * @param src
     * @return
     */
    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    /**
     * 用户输入->表单提交（响应结果）  用户输入+固定salt  第一次MD5加密
     * 将用户输入的明文密码与固定盐进行拼装后再进行MD5加密
     *
     * @param inputPass
     * @return
     */
    public static String inputPassToFormPass(String inputPass) {
        String str = "" + SALT.charAt(0) + SALT.charAt(2) + inputPass + SALT.charAt(5) + SALT.charAt(4);
        return md5(str);
    }

    /**
     * 表单单提交给服务器，服务器将表单提交数据--->数据库中存储的数据库 +随机salt 第二次MD5加墨
     * 将form表单中的密码转换成数据库中存储的密码
     */
    public static String formPassToDBPass(String formPass, String saltDB) {
        String str = "" + SALT.charAt(0) + SALT.charAt(2) + formPass + SALT.charAt(5) + SALT.charAt(4);
        return md5(str);
    }

    //将两次MD5加密进行整合(用户输入密码--->存储到服务器中密码)
    public static String inputPassToDBPass(String input, String saltDB) {
        String formPass = inputPassToFormPass(input);
        String dbPass = formPassToDBPass(formPass, saltDB);
        return dbPass;
    }


    public static void main(String[] args) {
        System.out.println(inputPassToFormPass("123456"));//d3b1294a61a07da9b49b6e22b2cbd7f9
        System.out.println(formPassToDBPass(inputPassToFormPass("123456"), "1a2b3c4d"));
        System.out.println(inputPassToDBPass("123456", "1a2b3c4d"));//b7797cce01b4b131b433b6acf4add449
    }
}
