package org.seckill.service.impl;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/3/3.
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    //��־����
    private Logger logger= LoggerFactory.getLogger(this.getClass());

    //����һ�������ַ���(��ɱ�ӿ�)��salt��Ϊ���ұ����û��³����ǵ�md5ֵ��ֵ�������Խ����Խ��
    private final String salt="shsdssljdd'l.";

    //ע��Service����
    @Autowired //@Resource
    private SeckillDao seckillDao;

    @Autowired //@Resource
    private SuccessKilledDao successKilledDao;

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill=seckillDao.queryById(seckillId);
        if (seckill==null) //˵���鲻�������ɱ��Ʒ�ļ�¼
        {
            return new Exposer(false,seckillId);
        }

        //������ɱδ����
        Date startTime=seckill.getStartTime();
        Date endTime=seckill.getEndTime();
        //ϵͳ��ǰʱ��
        Date nowTime=new Date();
        if (startTime.getTime()>nowTime.getTime() || endTime.getTime()<nowTime.getTime())
        {
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }

        //��ɱ������������ɱ��Ʒ��id���ø��ӿڼ��ܵ�md5
        String md5=getMD5(seckillId);
        return new Exposer(true,md5,seckillId);
    }

    private String getMD5(long seckillId)
    {
        String base=seckillId+"/"+salt;
        String md5= DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    //��ɱ�Ƿ�ɹ����ɹ�:����棬������ϸ��ʧ��:�׳��쳣������ع�
    @Transactional
    /**
     * ʹ��ע��������񷽷����ŵ�:
     * 1.�����ŶӴ��һ��Լ������ȷ��ע���񷽷��ı�̷��
     * 2.��֤���񷽷���ִ��ʱ�価���̣ܶ���Ҫ���������������RPC/HTTP������߰��뵽���񷽷��ⲿ
     * 3.�������еķ�������Ҫ������ֻ��һ���޸Ĳ�����ֻ��������Ҫ�������
     */
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {

        if (md5==null||!md5.equals(getMD5(seckillId)))
        {
            throw new SeckillException("seckill data rewrite");//��ɱ���ݱ���д��
        }
        //ִ����ɱ�߼�:�����+���ӹ�����ϸ
        Date nowTime=new Date();

        try{
            //�����
            int updateCount=seckillDao.reduceNumber(seckillId,nowTime);
            if (updateCount<=0)
            {
                //û�и��¿���¼��˵����ɱ����
                throw new SeckillCloseException("seckill is closed");
            }else {
                //��������˿�棬��ɱ�ɹ�,������ϸ
                int insertCount=successKilledDao.insertSuccessKilled(seckillId,userPhone);
                //���Ƿ����ϸ���ظ����룬���û��Ƿ��ظ���ɱ
                if (insertCount<=0)
                {
                    throw new RepeatKillException("seckill repeated");
                }else {
                    //��ɱ�ɹ�,�õ��ɹ��������ϸ��¼,�����سɹ���ɱ����Ϣ
                    SuccessKilled successKilled=successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS,successKilled);
                }
            }

        }catch (SeckillCloseException e1)
        {
            throw e1;
        }catch (RepeatKillException e2)
        {
            throw e2;
        }catch (Exception e)
        {
            logger.error(e.getMessage(),e);
            //���Ա������쳣ת��Ϊ�������쳣
            throw new SeckillException("seckill inner error :"+e.getMessage());
        }

    }
}
