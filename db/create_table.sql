--创建字典表
create table sys_dict_key
(
  dict_key varchar(64) comment '字典类型' primary key,
  name     varchar(256) comment '类型名称'
);

--创建字典值
create table sys_dict_value
(
  dict_key   varchar(64) comment '字典类型',
  dict_value varchar(64) comment '字典值',
  value_name varchar(256) comment '值名称',
);

--创建交易流水表
-- auto-generated definition
create table trade_detail
(
  trade_no     varchar(32)                         not null comment '交易流水号',
  trade_type   varchar(1)                          null comment '交易类型: 1-收入，2-支出',
  trade_class  varchar(4)                          null comment '交易种类',
  trade_sum    decimal(20, 10)                     null comment '交易金额',
  trade_date   datetime                            null comment '交易时间',
  data_source  varchar(4)                          null comment '数据来源',
  remark       varchar(512)                        null comment '备注信息',
  created_date timestamp default CURRENT_TIMESTAMP null comment '数据插入时间
',
  constraint trade_detail_trade_no_uindex
    unique (trade_no)
)
  comment '交易流水表';

create index ix_trade_detial_tc
  on trade_detail (trade_type, trade_class);

alter table trade_detail
  add primary key (trade_no);


