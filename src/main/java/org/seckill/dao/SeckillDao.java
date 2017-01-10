package org.seckill.dao;

import java.util.Date;

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


}
