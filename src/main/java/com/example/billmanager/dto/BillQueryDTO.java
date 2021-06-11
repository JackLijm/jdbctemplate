package com.example.billmanager.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BillQueryDTO extends BaseDTO {
    private String tradeTo;
    private String goodsName;
    private Date startDate;
    private Date endDate;
    private String tradeType;
    private String dataSource;
    private String mainLabel;
    private String subLabel;
    private Date month;
    private List<String> mainLabelList;
}
