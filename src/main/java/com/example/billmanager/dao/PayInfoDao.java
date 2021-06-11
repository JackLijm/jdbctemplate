package com.example.billmanager.dao;

import  com.example.billmanager.brickyard.dto.PayInfoDto;
import com.example.billmanager.brickyard.dto.QueryParamDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PayInfoDao {
    int insertPayInfo(PayInfoDto record);

    List<PayInfoDto> queryPayInfoList(QueryParamDto queryParamDto);

    int deletePayInfo(QueryParamDto queryParamDto);
}