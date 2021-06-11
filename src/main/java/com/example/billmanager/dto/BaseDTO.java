package com.example.billmanager.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BaseDTO {

    private Date createdDate;

    private String createdBy;

    private Date updatedDate;

    private String updatedBy;

    private String dataState;

    private String operateType;

    private int pageSize;
    private int pageNum;
}
