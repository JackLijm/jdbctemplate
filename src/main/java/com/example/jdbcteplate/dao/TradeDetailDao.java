package com.example.jdbcteplate.dao;

import com.example.jdbcteplate.dto.TradeDetailDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TradeDetailDao {
    int deleteByPrimaryKey(int tradeNo);

    int insert(TradeDetailDTO record);

    int insertSelective(TradeDetailDTO record);

    TradeDetailDTO selectByPrimaryKey(String tradeNo);

    int updateByPrimaryKeySelective(TradeDetailDTO record);

    int updateByPrimaryKey(TradeDetailDTO record);

    List<TradeDetailDTO> query();

    /**
     * 收入-支出报表
     * @return
     */
    List<Map<String,Object>>  groupByTradeType();

}