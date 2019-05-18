package com.github.soyanga.secondskillsystem.controller;

import com.github.soyanga.secondskillsystem.domain.MiaoShaUser;
import com.github.soyanga.secondskillsystem.redis.RedisService;
import com.github.soyanga.secondskillsystem.result.Result;
import com.github.soyanga.secondskillsystem.service.GoodsService;
import com.github.soyanga.secondskillsystem.service.MiaoshaUserService;
import com.github.soyanga.secondskillsystem.util.ValidatorUtil;
import com.github.soyanga.secondskillsystem.vo.GoodsVo;
import com.github.soyanga.secondskillsystem.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @program: seconds_kill_system _2
 * @Description: 登陆成功挑转到商品页面
 * @Author: SOYANGA
 * @Create: 2019-05-17 17:34
 * @Version 1.0
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {


    @Autowired
    RedisService redisService;

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    GoodsService goodsService;

    /**
     * 商品列表页 单点登陆 User已将通过UserArgumentResolver解析并通过WebConfig加入到解析队列中
     *
     * @return 从登录页面 跳转至 商品页面
     */
    @RequestMapping("/to_list")
    public String toGoodsList(MiaoShaUser user, Model model) {
        model.addAttribute("user", user);
        //查询商品列表
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        return "goods_list";
    }

    /**
     * 商品详情页
     *
     * @return 从商品页面 跳转至 商品详情页
     */
    @RequestMapping("/to_detail/{goodsId}")
    public String toDetail(MiaoShaUser user, Model model,
                           @PathVariable("goodsId") long goodsId) {
        //snowflake 64位自增长ID算法 来设计ID
        model.addAttribute("user", user);

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        //秒杀是否开始，还有多少时间
        long starterAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        //表示秒杀的状态 0没有开始，1已经开始 2已将结束了
        int miaoshaStatus = 0;
        //剩余多少时间
        long remainSeconds = 0;
        if (now < starterAt) {
            //秒杀没有开始,倒计时
            miaoshaStatus = 0;
            remainSeconds = (starterAt - now) / 1000;
        } else if (now > endAt) {
            //秒杀结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {
            //秒杀正在进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }


//    /**
//     * 商品列表页 单点登陆
//     *
//     * @return
//     */
//    @RequestMapping("/to_list")
//    public String toGoodsList(HttpServletResponse response,
//                              Model model,
//                              @CookieValue(value = MiaoshaUserService.COOKIE_NAME_TOKEN, required = false) String cookieToken,
//                              @RequestParam(value = MiaoshaUserService.COOKIE_NAME_TOKEN, required = false) String paramToken) {
//        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
//            return "login";
//        }
//        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
//        MiaoShaUser user = userService.getByToken(response, token);
//        model.addAttribute("user", user);
//        return "goods_list";
//    }

}