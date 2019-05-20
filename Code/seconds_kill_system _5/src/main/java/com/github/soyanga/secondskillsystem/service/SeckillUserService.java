package com.github.soyanga.secondskillsystem.service;

import com.github.soyanga.secondskillsystem.dao.SeckillUserDao;
import com.github.soyanga.secondskillsystem.domain.SeckillUser;
import com.github.soyanga.secondskillsystem.exception.GlobalException;
import com.github.soyanga.secondskillsystem.redis.SeckillUserkey;
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
public class SeckillUserService {

    public static final String COOKIE_NAME_TOKEN = "token";


    @Autowired
    SeckillUserDao seckillUserDao;
    @Autowired
    RedisService redisService;

    /**
     * 通过手机号获取用户对象
     * 优化：对象缓存  减少用户访问数据库的次数
     *
     * @param id
     * @return
     */
    public SeckillUser getById(long id) {
        //取缓存
        SeckillUser user = redisService.get(SeckillUserkey.getById, "" + id, SeckillUser.class);
        if (user != null) {
            return user;
        }
        //缓存中并没有该信息，就需要从数据库中取然后返回，并放到缓存中以便于下次使用
        user = seckillUserDao.getById(id);
        if (user != null) {
            redisService.set(SeckillUserkey.getById, "" + id, user);
        }
        return user;
    }

    /**
     * 更新用户密码：用户修改自己的密码 有待优化未使用注册所以该方法未使用
     *
     * @param id
     * @param formPassNew
     * @return
     */
    public boolean updatePassword(String token, long id, String formPassNew) {
        //取user对象
        SeckillUser user = getById(id);
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIT);
        }
        //更新mysql数据库
        SeckillUser toBeUpdate = new SeckillUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPassNew, user.getSalt()));
        seckillUserDao.update(toBeUpdate);
        //更新数据库成功了，我们就需要处理缓存 //TODO 跟新即可mysql和Redis即可？ 需不需要删除？
        redisService.delte(SeckillUserkey.getById, "" + id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(SeckillUserkey.token, token, user);
        return true;
    }


    /**
     * 登陆操作 实现单点登陆 分布式Session
     *
     * @param loginVo 用户输入 （密码为MD5一次加密后）
     * @return 返回结果信息
     */
    public String login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        SeckillUser user = getById(Long.parseLong(mobile));
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
        return token;
    }


    /**
     * 实现单点登陆（核心）分布式Session
     *
     * @param response
     * @param user
     */
    private void addCookie(HttpServletResponse response, String token, SeckillUser user) {
        //标识token对应的是那个用户
        //讲用户信息写到Redis当中 ()
        redisService.set(SeckillUserkey.token, token, user);
        //2.生成Cookie将token写给浏览器
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        //设置Cookie过期时间，过期时间为2天
        cookie.setMaxAge(SeckillUserkey.token.expireSeconds());
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
    public SeckillUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        SeckillUser user = redisService.get(SeckillUserkey.token, token, SeckillUser.class);
        //延长有效期
        if (user != null) {
            addCookie(response, token, user);
        }
        return user;
    }
}
