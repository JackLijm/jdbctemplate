package com.example.billmanager.brickyard.dto;

import com.example.billmanager.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * pay_info
 * @author 
 */
@Data
public class PayInfoDto extends BaseDTO {
    private String paySerialNo;

    private String bookingId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date payDate;

    private BigDecimal payAmount;

    private String operator;

    private String remark;

    /**
     * 接口字段
     */
    private String isClear;

    /**
     * 接口字段
     */
    private BigDecimal paySum;

}