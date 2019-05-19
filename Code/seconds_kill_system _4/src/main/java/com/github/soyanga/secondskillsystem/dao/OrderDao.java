package com.github.soyanga.secondskillsystem.dao;

import com.github.soyanga.secondskillsystem.domain.MiaoshaOrder;
import com.github.soyanga.secondskillsystem.domain.OrderInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * @program: seconds_kill_system _3
 * @Description: 订单相关的数据库操作
 * @Author: SOYANGA
 * @Create: 2019-05-18 17:59
 * @Version 1.0
 */
@Repository
@Mapper
public interface OrderDao {

    /**
     * 通过用户id ，商品id查询秒杀订单信息 是否存在（判断用户是否已将秒杀了该商品）
     *
     * @param userId
     * @param goodsId
     * @return 秒杀订单信息
     */
    @Select("select * from miaosha_order where user_id=#{userId} and goods_id=#{goodsId}")
    MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

    /**
     * 将生成订单信息
     *
     * @param orderInfo
     * @return 返回插入信息后的主键(id)订单编号（给秒杀订单设置订单编号使用）
     */
    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values("
            + "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
    long insert(OrderInfo orderInfo);

    /**
     * 生成秒杀订单信息
     *
     * @param miaoshaOrder
     * @return
     */
    @Insert("insert into miaosha_order (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
    int insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);
}
