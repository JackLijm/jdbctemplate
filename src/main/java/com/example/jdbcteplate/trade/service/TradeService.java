package com.example.jdbcteplate.trade.service;

import com.example.jdbcteplate.dto.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * 交易相关接口
 */
public interface TradeService {
    /**
     * 插入交易记录
     *
     * @param tradeDetailDTO
     * @return
     */
    public int addTradeDetail(TradeDetailDTO tradeDetailDTO);

    /**
     * 修改交易记录
     *
     * @param tradeDetailDTO
     * @return
     */
    public int updateTradeDetail(TradeDetailDTO tradeDetailDTO);

    /**
     * 删除交易记录
     *
     * @param tradeDetailDTO
     * @return
     */
    public int delTradeDetail(TradeDetailDTO tradeDetailDTO);

    /**
     * 查询交易记录
     * @param baseDTO
     * @return
     */
    public CommonPageDTO queryTradeDetail(BaseDTO baseDTO);

    /**
     * 导入excel
     * @param baseDTO
     * @return
     */
    void tempImportExcel(BaseDTO baseDTO) throws FileNotFoundException, IOException;

    /**
     * 查看月份收入-支出报表
     * @return
     */
    EChartsDTO tradeTypeReport();

    /**
     * 查看月份分类报表
     * @return
     */
    EChartsDTO monthTradeTypeReport(BillQueryDTO billQueryDTO);

    /**
     * 查询账单数据
     * @param baseDTO
     * @return
     */
    CommonPageDTO queryBillList(BillQueryDTO baseDTO);

    /**
     * 插入标签
     * @param baseDTO
     * @return
     */
    int addBillLabel(BillDetailDTO baseDTO);


    /**
     * 查看每个月收支情况
     *
     * @return
     */
    EChartsDTO monthTradeData(BillQueryDTO billQueryDTO);
}
