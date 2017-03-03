package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import java.util.List;


/**
 * Created by Administrator on 2017/3/3.
 */
public interface SeckillService {
    /**
     * ��ѯȫ������ɱ��¼
     *
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * ��ѯ������ɱ��¼
     *
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);


    //�����£�����������Ҫ����Ϊ��һЩ�ӿ�

    /**
     * ����ɱ����ʱ�����ɱ�ӿڵĵ�ַ���������ϵͳʱ�����ɱʱ��
     *
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException,RepeatKillException,SeckillCloseException;

}
