use seckill;


CREATE TABLE seckill(
  `seckill_id` BIGINT NOT NUll AUTO_INCREMENT COMMENT '��Ʒ���ID',
  `name` VARCHAR(120) NOT NULL COMMENT '��Ʒ����',
  `number` int NOT NULL COMMENT '�������',
  `start_time` TIMESTAMP  NOT NULL COMMENT '��ɱ��ʼʱ��',
  `end_time`   TIMESTAMP  NOT NULL COMMENT '��ɱ����ʱ��',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
  PRIMARY KEY (seckill_id),
  key idx_start_time(start_time),
  key idx_end_time(end_time),
  key idx_create_time(create_time)
)ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='��ɱ����';

insert into seckill
(name,number,start_time,end_time)
VALUES
  ('1000Ԫ��ɱiphone6',100,'2017-1-1 00:00:00','2017-1-2 00:00:00'),
  ('500Ԫ��ɱiphone2',200,'2017-1-1 00:00:00','2017-11-2 00:00:00'),
  ('300Ԫ��ɱxiaomi4',300,'2017-1-1 00:00:00','2017-2-2 00:00:00'),
  ('200Ԫ��ɱ����note',400,'2017-11-1 00:00:00','2017-11-2 00:00:00');

-- ��ɱ�ɹ���ϸ��
-- �û���¼��֤�����Ϣ(��Ϊ�ֻ���)
CREATE TABLE success_killed(
  `seckill_id` BIGINT NOT NULL COMMENT '��ɱ��ƷID',
  `user_phone` BIGINT NOT NULL COMMENT '�û��ֻ���',
  `state` TINYINT NOT NULL DEFAULT -1 COMMENT '״̬��ʶ:-1:��Ч 0:�ɹ� 1:�Ѹ��� 2:�ѷ���',
  `create_time` TIMESTAMP NOT NULL COMMENT '����ʱ��',
  PRIMARY KEY(seckill_id,user_phone),/*��������*/
  KEY idx_create_time(create_time)
)ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='��ɱ�ɹ���ϸ��'