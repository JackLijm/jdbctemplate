package com.example.billmanager.dao;


import com.example.billmanager.dto.BillDetailDTO;
import com.example.billmanager.dto.BillQueryDTO;
import com.example.billmanager.dto.MonthDataDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BillDetailDao {

    int insert(BillDetailDTO record);

    int insertSelective(BillDetailDTO record);

    List<BillDetailDTO> queryList(BillQueryDTO baseDTO);

    int updateBillLabel(BillDetailDTO baseDTO);

    /**
     * 收入-支出报表
     *
     * @return
     */
    List<Map<String, Object>> groupByTradeType();


    /**
     * 获取每月的交易数据
     *
     * @return
     */
    List<MonthDataDTO> queryMonthData(Map<String, Object> queryParam);
}