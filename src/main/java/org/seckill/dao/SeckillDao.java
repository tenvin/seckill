package org.seckill.dao;

import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;

/**
 * Created by HP on 2017/1/10.
 */
public interface SeckillDao {

    /**
     * @param seckillId
     * @param killTime
     * @return
     */
    int reduceNumber(long seckillId,Date killTime);

    /**
     * 根据id查询秒杀的商品信息
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    List<Seckill> queryAll(int offset,int limit);

}
