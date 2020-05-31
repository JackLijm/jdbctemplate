package com.example.jdbcteplate.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class EChartsDTO {
    private List<String> xAxis;
    private List<Object> yAxis;
    private BigDecimal max;
    private BigDecimal min;
    private List<Object> data;
    private List<String> dataTitle;
}
