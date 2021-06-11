package com.example.billmanager.brickyard.dto;

import com.example.billmanager.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * booking_info
 * @author 
 */
@Data
public class BookingInfoDto extends BaseDTO {
    private String bookingId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date applyDate;

    private String clientName;

    private String clientAddr;

    private String brickType;

    private BigDecimal tradeCount;

    private BigDecimal tradeUnit;

    private String isClear;

    private String goodSender;

    /**
     * 应收款 = 数量*单价
     */
    private BigDecimal price;

    /**
     * 实收款 = 已收款
     */
    private BigDecimal paySum;

}