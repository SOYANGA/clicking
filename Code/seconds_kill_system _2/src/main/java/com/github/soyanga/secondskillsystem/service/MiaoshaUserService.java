package com.github.soyanga.secondskillsystem.service;

import com.github.soyanga.secondskillsystem.dao.MiaoshaUserDao;
import com.github.soyanga.secondskillsystem.domain.MiaoShaUser;
import com.github.soyanga.secondskillsystem.exception.GlobalException;
import com.github.soyanga.secondskillsystem.result.CodeMsg;
import com.github.soyanga.secondskillsystem.result.Result;
import com.github.soyanga.secondskillsystem.util.MD5Util;
import com.github.soyanga.secondskillsystem.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: seconds_kill_system _2
 * @Description: 秒杀用户实体类
 * @Author: SOYANGA
 * @Create: 2019-05-16 15:00
 * @Version 1.0
 */
@Service
public class MiaoshaUserService {


    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    /**
     * 通过手机号获取用户对象
     *
     * @param id
     * @return
     */
    public MiaoShaUser getById(long id) {
        return miaoshaUserDao.getById(id);
    }

    /**
     * 登陆操作
     *
     * @param loginVo 用户输入 （密码为MD5一次加密后）
     * @return 返回结果信息
     */
    public boolean login(LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        MiaoShaUser user = getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIT);
        }
        //验证密码--数据库中的密码跟用户输入密码是否一致
        String dbPass = user.getPassword();
        //数据库中存储的盐
        String saltDB = user.getSalt();
        //将前台加密后的密码转换成数据库存储的二次加密密码
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
        if (!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        return true;
    }
}
