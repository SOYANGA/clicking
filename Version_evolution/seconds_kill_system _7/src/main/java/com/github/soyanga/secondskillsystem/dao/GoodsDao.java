package com.github.soyanga.secondskillsystem.dao;

import com.github.soyanga.secondskillsystem.domain.SeckillGoods;
import com.github.soyanga.secondskillsystem.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: seconds_kill_system _3
 * @Description: 关于商品的DAO等操作
 * @Author: SOYANGA
 * @Create: 2019-05-18 14:40
 * @Version 1.0
 */
@Mapper
@Repository
public interface GoodsDao {

    /**
     * 查询商品所有的信息
     *
     * @return
     */
    @Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.seckill_price from seckill_goods mg left join goods g on mg.goods_id = g.id")
    List<GoodsVo> listGoodsVo();

    /**
     * 通过商品ID查询商品信息 返回商品详情信息
     *
     * @param goodsId
     * @return
     */
    @Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.seckill_price from seckill_goods mg left join goods g on mg.goods_id = g.id where g.id = #{goodsId}")
    GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

    /**
     * 减少商品库存
     * 数据库本身就会给数据库进行修改时加一把锁，保证了减库存操作的互斥性
     * 数据库唯一索引放置用户重复下单（一个用户同时发出两个请求）
     *
     * @param g
     * @return 返回修改的行数
     */
    @Update("update seckill_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0")
    int reduceStock(SeckillGoods g);


    @Update("update seckill_goods set stock_count = #{stockCount} where goods_id = #{goodsId}")
    public int resetStock(SeckillGoods g);
}
