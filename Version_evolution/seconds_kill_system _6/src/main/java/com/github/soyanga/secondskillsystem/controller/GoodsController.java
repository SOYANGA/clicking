package com.github.soyanga.secondskillsystem.controller;

import com.github.soyanga.secondskillsystem.domain.SeckillUser;
import com.github.soyanga.secondskillsystem.redis.GoodsKey;
import com.github.soyanga.secondskillsystem.redis.RedisService;
import com.github.soyanga.secondskillsystem.result.Result;
import com.github.soyanga.secondskillsystem.service.GoodsService;
import com.github.soyanga.secondskillsystem.service.SeckillUserService;
import com.github.soyanga.secondskillsystem.util.ValidatorUtil;
import com.github.soyanga.secondskillsystem.vo.GoodsDetailVo;
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
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
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
    SeckillUserService userService;

    @Autowired
    GoodsService goodsService;
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;


    //qps:632.4        2500 *  5   error 0
    //qps:545.7        5000 * 10   error 2.87%

    //qps:1013.3      2500*5
    //qps:887         5000*10

    /**
     * 商品列表页 单点登陆 User已将通过UserArgumentResolver解析并通过WebConfig加入到解析队列中
     * <p>
     * 页面缓存
     * 优化：返回的是渲染好的html（浏览器可以直接进行缓存并将 Html放入响应的Body中，缓存中没有再手动渲染（60s有效期））
     *
     * @return 从登录页面 跳转至 商品页面
     */
    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String toGoodsList(HttpServletResponse response, HttpServletRequest request, SeckillUser user, Model model) {
        //1.从缓存中先去取
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        //2.缓存中并没有，手动渲染,再放到缓存中去

        //2.1生成thyeleamfy页面所需要的资源
        model.addAttribute("user", user);
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);

        IWebContext context = new WebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap());
        //2.2将 thymeleaf经过Template模板引擎进行手动渲染，最终将其放入到Body中响应给浏览器
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", context);
        //2.3放入缓存中
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }


    /**
     * 商品详情页
     * <p>
     * 优化：同上对页面进行了缓存（60s）的生命周期 url缓存（不同商品有不同的页面缓存）
     *
     * @return 从商品页面 跳转至 商品详情页
     */
    @RequestMapping(value = "/to_detail2/{goodsId}", produces = "text/html")
    @ResponseBody
    public String toDetail2(HttpServletResponse response, HttpServletRequest request, SeckillUser user, Model model,
                            @PathVariable("goodsId") long goodsId) {
        //snowflake 64位自增长ID算法 来设计ID
        //1.从缓存中先去取
        String html = redisService.get(GoodsKey.getGoodsDetail, "" + goodsId, String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        //2.缓存中并没有，手动渲染,再放到缓存中去
        model.addAttribute("user", user);
        //2.1生成渲染thyeleamf所需要资源
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        //秒杀是否开始，还有多少时间
        long starterAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        //表示秒杀的状态 0没有开始，1已经开始 2已将结束了
        int seckillStatus = 0;
        //剩余多少时间
        long remainSeconds = 0;
        if (now < starterAt) {
            //秒杀没有开始,倒计时
            seckillStatus = 0;
            remainSeconds = (starterAt - now) / 1000;
        } else if (now > endAt) {
            //秒杀结束
            seckillStatus = 2;
            remainSeconds = -1;
        } else {
            //秒杀正在进行中
            seckillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("seckillStatus", seckillStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        IWebContext context = new WebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap());
        //2.2将 thymeleaf经过Template模板引擎进行手动渲染，最终将其放入到Body中响应给浏览器
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", context);
        //2.3放入缓存中
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsDetail, "" + goodsId, html);
        }
        return html;
    }


    /**
     * 商品详情页  页面静态化
     * <p>
     * 优化：同上对页面进行了缓存（60s）的生命周期 url缓存（不同商品有不同的页面缓存）
     *
     * @return 从商品页面 跳转至 商品详情页
     */
    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> toDetail(HttpServletResponse response, HttpServletRequest request, SeckillUser user, Model model, @PathVariable("goodsId") long goodsId) {
        //snowflake 64位自增长ID算法 来设计ID
        //从数据库中通过goodsId来获取goods所有信息
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        //秒杀是否开始，还有多少时间
        long starterAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        //表示秒杀的状态 0没有开始，1已经开始 2已将结束了
        int seckillStatus = 0;
        //剩余多少时间
        long remainSeconds = 0;
        if (now < starterAt) {
            //秒杀没有开始,倒计时
            seckillStatus = 0;
            remainSeconds = (starterAt - now) / 1000;
        } else if (now > endAt) {
            //秒杀结束
            seckillStatus = 2;
            remainSeconds = -1;
        } else {
            //秒杀正在进行中
            seckillStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setGoods(goods);
        goodsDetailVo.setRemainSeconds(remainSeconds);
        goodsDetailVo.setSeckillStatus(seckillStatus);
        goodsDetailVo.setUser(user);
        //JSON数据
        return Result.success(goodsDetailVo);
    }


//    /**
//     * 商品列表页 单点登陆
//     *
//     * @return
//     */
//    @RequestMapping("/to_list")
//    public String toGoodsList(HttpServletResponse response,
//                              Model model,
//                              @CookieValue(value = SeckillUserService.COOKIE_NAME_TOKEN, required = false) String cookieToken,
//                              @RequestParam(value = SeckillUserService.COOKIE_NAME_TOKEN, required = false) String paramToken) {
//        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
//            return "login";
//        }
//        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
//        SeckillUser user = userService.getByToken(response, token);
//        model.addAttribute("user", user);
//        return "goods_list";
//    }

}