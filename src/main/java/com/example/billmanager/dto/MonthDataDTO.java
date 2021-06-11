package com.example.billmanager.dto;

import lombok.Data;

@Data
public class MonthDataDTO {
    /**
     * 交易分组
     */
    private String tradeType;
    /**
     * 交易日期
     */
    private String tradeMonth;
    /**
     * 交易笔数
     */
    private String tradeCount;
    /**
     * 交易汇总
     */
    private String tradeSum;
}
