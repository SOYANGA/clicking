package com.github.soyanga.secondskillsystem.service;

import com.github.soyanga.secondskillsystem.dao.MiaoshaUserDao;
import com.github.soyanga.secondskillsystem.domain.MiaoShaUser;
import com.github.soyanga.secondskillsystem.exception.GlobalException;
import com.github.soyanga.secondskillsystem.redis.MiaoshaUserkey;
import com.github.soyanga.secondskillsystem.redis.RedisService;
import com.github.soyanga.secondskillsystem.result.CodeMsg;
import com.github.soyanga.secondskillsystem.util.MD5Util;
import com.github.soyanga.secondskillsystem.util.UUIDUtil;
import com.github.soyanga.secondskillsystem.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: seconds_kill_system _2
 * @Description: 秒杀用户实体类
 * @Author: SOYANGA
 * @Create: 2019-05-16 15:00
 * @Version 1.0
 */
@Service
public class MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";


    @Autowired
    MiaoshaUserDao miaoshaUserDao;
    @Autowired
    RedisService redisService;

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
     * 登陆操作 实现单点登陆 分布式Session
     *
     * @param loginVo 用户输入 （密码为MD5一次加密后）
     * @return 返回结果信息
     */
    public boolean login(HttpServletResponse response, LoginVo loginVo) {
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
        //1.登陆成功生成一个cookie 包含一个随机的用户ID  token   用于实现单点登陆
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return true;
    }


    /**
     * 实现单点登陆（核心）分布式Session
     *
     * @param response
     * @param user
     */
    private void addCookie(HttpServletResponse response, String token, MiaoShaUser user) {
        //标识token对应的是那个用户
        //讲用户信息写到Redis当中 ()
        redisService.set(MiaoshaUserkey.token, token, user);
        //2.生成Cookie将token写给浏览器
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        //设置Cookie过期时间，过期时间为2天
        cookie.setMaxAge(MiaoshaUserkey.token.expireSeconds());
        //cookie作用域
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 从Redis缓存中取出对应token的User对象
     *
     * @param token 令牌（SessionID）
     * @return User对象
     */
    public MiaoShaUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoShaUser user = redisService.get(MiaoshaUserkey.token, token, MiaoShaUser.class);
        //延长有效期
        if (user != null) {
            addCookie(response, token, user);
        }
        return user;
    }
}
