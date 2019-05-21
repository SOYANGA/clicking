package com.github.soyanga.secondskillsystem.service;

import com.github.soyanga.secondskillsystem.dao.GoodsDao;
import com.github.soyanga.secondskillsystem.domain.Goods;
import com.github.soyanga.secondskillsystem.domain.SeckillGoods;
import com.github.soyanga.secondskillsystem.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @program: seconds_kill_system _3
 * @Description: 商品服务
 * @Author: SOYANGA
 * @Create: 2019-05-18 14:39
 * @Version 1.0
 */
@Service
public class GoodsService {

    @Autowired
    GoodsDao goodsDao;

    /**
     * 展示商品所有信息（包括秒杀商品信息）
     *
     * @return
     */
    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }

    /**
     * 通过商品id获取商品所有信息（用于跳转商品详情）
     *
     * @param goodsId
     * @return
     */
    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    /**
     * 秒杀成功第一步：减少库存
     *
     * @param goods
     */
    @Transactional(rollbackFor = Exception.class) //需要显示回滚
    public boolean reduceStock(GoodsVo goods) {
        SeckillGoods g = new SeckillGoods();
        g.setGoodsId(goods.getId());
        int ret = goodsDao.reduceStock(g);
        return ret > 0;
    }


    public void resetStock(List<GoodsVo> goodsList) {
        for(GoodsVo goods : goodsList ) {
            SeckillGoods g = new SeckillGoods();
            g.setGoodsId(goods.getId());
            g.setStockCount(goods.getStockCount());
            goodsDao.resetStock(g);
        }
    }
}
