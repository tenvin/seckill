package org.seckill.dao;

import org.seckill.entity.SuccessKilled;

/**
 * Created by Administrator on 2017/3/2.
 */
public interface SuccessKilledDao {

    int insertSuccessKilled(long seckillId ,long userPhone);

    SuccessKilled queryByIdWithSeckill(long seckillid);
}
