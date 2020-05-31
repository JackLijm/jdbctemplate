package com.example.jdbcteplate.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BillDetailDTO {
    /**
     * 自增流水号
     */
    private String serialNo;
    /**
     * 交易账号
     */
    private String accountNo;

    /**
     * 交易号
     */
    private String tradeNo;

    /**
     * 交易时间
     */
    private Date tradeTime;

    /**
     * 交易对方
     */
    private String tradeTo;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 交易金额
     */
    private BigDecimal tradeSum;

    /**
     * 交易类型
     */
    private String tradeType;

    /**
     * 交易状态
     */
    private String tradeStatus;

    /**
     * 资金状态
     */
    private String remark;

    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 导入批次号
     */
    private String importBatch;

    /**
     * 一级标签
     */
    private String mainLabel;

    /**
     * 二级标签
     */
    private String subLabel;

}
