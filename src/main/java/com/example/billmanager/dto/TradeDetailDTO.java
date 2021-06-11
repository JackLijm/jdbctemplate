package com.example.billmanager.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * trade_detail
 * @author 
 */
@Data
public class TradeDetailDTO implements Serializable  {
    /**
     * 交易流水号
     */
    private int tradeNo;

    /**
     * 交易类型: 1-收入，2-支出
     */
    private String tradeType;

    /**
     * 交易种类
     */
    private String tradeClass;

    /**
     * 交易金额
     */
    private BigDecimal tradeSum;

    /**
     * 交易时间
     */
    private Date tradeDate;

    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 数据插入时间

     */
    private Date createdDate;

    private static final long serialVersionUID = 1L;
}