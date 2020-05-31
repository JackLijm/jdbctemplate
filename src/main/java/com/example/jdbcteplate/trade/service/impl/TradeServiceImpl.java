package com.example.jdbcteplate.trade.service.impl;

import com.example.jdbcteplate.dao.BillDetailDao;
import com.example.jdbcteplate.dao.TradeDetailDao;
import com.example.jdbcteplate.dto.*;
import com.example.jdbcteplate.trade.service.TradeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TradeServiceImpl implements TradeService {

    @Autowired
    TradeDetailDao tradeDetailDao;
    @Autowired
    BillDetailDao billDetailDao;

    /**
     * 插入交易记录
     *
     * @param tradeDetailDTO
     * @return
     */
    @Override
    public int addTradeDetail(TradeDetailDTO tradeDetailDTO) {
        return tradeDetailDao.insert(tradeDetailDTO);
    }

    /**
     * 修改交易记录
     *
     * @param tradeDetailDTO
     * @return
     */
    @Override
    public int updateTradeDetail(TradeDetailDTO tradeDetailDTO) {
        return tradeDetailDao.updateByPrimaryKeySelective(tradeDetailDTO);
    }

    /**
     * 删除交易记录
     *
     * @param tradeDetailDTO
     * @return
     */
    @Override
    public int delTradeDetail(TradeDetailDTO tradeDetailDTO) {
        return tradeDetailDao.deleteByPrimaryKey(tradeDetailDTO.getTradeNo());
    }

    /**
     * 查询交易记录
     *
     * @param baseDTO
     * @return
     */
    @Override
    public CommonPageDTO queryTradeDetail(BaseDTO baseDTO) {
        PageHelper.startPage(baseDTO.getPageNum(), baseDTO.getPageSize());
        List<TradeDetailDTO> query = tradeDetailDao.query();
        PageInfo<TradeDetailDTO> pageInfo = new PageInfo<>(query);
        return new CommonPageDTO(pageInfo);
    }

    /**
     * 导入excel
     *
     * @param baseDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tempImportExcel(BaseDTO baseDTO) throws IOException {
        XSSFWorkbook xssfWorkbook = null;
        FileInputStream fileInputStream = null;
        try {
            //1.读取xlsx文件
            fileInputStream = new FileInputStream("C:\\Users\\Administrator\\Desktop\\xxx2.xlsx");
            xssfWorkbook = new XSSFWorkbook(fileInputStream);
            //2.获取sheet页
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(4);
            for (int i = 0; i <= xssfSheet.getLastRowNum(); i++) {
                //3.读每一行，
                XSSFRow xssfRow = xssfSheet.getRow(i);//获取每一行的数据
                Date dateCellValue = xssfRow.getCell(1).getDateCellValue();
                String tradeClass = xssfRow.getCell(2).getStringCellValue();
                double tradeSum = xssfRow.getCell(3).getNumericCellValue();
                String dataSource = xssfRow.getCell(4).getStringCellValue();
                String remark = xssfRow.getCell(5) == null ? "" : xssfRow.getCell(5).getStringCellValue();
                TradeDetailDTO tradeDetailDTO = new TradeDetailDTO();
                tradeDetailDTO.setTradeDate(changeDateTo2019(dateCellValue));
                tradeDetailDTO.setDataSource(dataSourceChange(dataSource));
                tradeDetailDTO.setRemark(remark);
                tradeDetailDTO.setTradeClass(tradeClassChange(tradeClass));
                tradeDetailDTO.setTradeSum(new BigDecimal(tradeSum));
                tradeDetailDTO.setTradeType("1");
                tradeDetailDao.insert(tradeDetailDTO);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            assert xssfWorkbook != null;
            fileInputStream.close();
            xssfWorkbook.close();
        }

    }

    /**
     * 查看月份收入-支出报表
     *
     * @return
     */
    @Override
    public EChartsDTO tradeTypeReport() {
        EChartsDTO eChartsDTO = new EChartsDTO();
        List<Map<String,Object>> tradeDetailDTOS = billDetailDao.groupByTradeType();
        //x轴
        List<String> xAxis = tradeDetailDTOS.stream().map(map -> (String) map.get("tradeDate")).distinct().sorted(String::compareTo).collect(Collectors.toList());
        List<Object> totalList = new ArrayList<>();
        List<String> dataTitle = new ArrayList<>();
        List<String> tradeType1 = new ArrayList<>();
        List<String> tradeType2 = new ArrayList<>();
        List<String> tradeType3 = new ArrayList<>();
        totalList.add(tradeType1);
        dataTitle.add("收入");
        totalList.add(tradeType2);
        dataTitle.add("支出");
        totalList.add(tradeType3);
        dataTitle.add("收入-支出");
        //y轴
        for (Object xAxi : xAxis) {
            Optional<Map<String, Object>> first1 = tradeDetailDTOS.stream().filter(map -> xAxi.equals(map.get("tradeDate")) && "收入".equals(map.get("tradeType"))).findFirst();
            String first1Value = "";
            String first2Value = "";
            if (first1.isPresent()){
                first1Value=String.valueOf(first1.get().get("tradeSum"));
            }else{
                first1Value = "0.00";
            }
            tradeType1.add(first1Value);
            Optional<Map<String, Object>> first2 = tradeDetailDTOS.stream().filter(map -> xAxi.equals(map.get("tradeDate")) && "支出".equals(map.get("tradeType"))).findFirst();
            if (first2.isPresent()){
                first2Value=String.valueOf(first2.get().get("tradeSum"));
            }else{
                first2Value="0.00";
            }
            tradeType2.add(first2Value);
            tradeType3.add(new BigDecimal(first1Value).subtract(new BigDecimal(first2Value)).toString());
        }

        BigDecimal max = tradeDetailDTOS.stream().map(map ->(BigDecimal) map.get("tradeSum")).max(BigDecimal::compareTo).orElse(new BigDecimal("0"));
        max = max.add(new BigDecimal("100"));
        BigDecimal min = tradeDetailDTOS.stream().map(map -> (BigDecimal) map.get("tradeSum")).min(BigDecimal::compareTo).orElse(new BigDecimal("0"));
        eChartsDTO.setXAxis(xAxis);
        eChartsDTO.setYAxis(totalList);
        eChartsDTO.setMax(max);
        eChartsDTO.setDataTitle(dataTitle);
        return eChartsDTO;
    }


    /**
     * 查看月份分类报表
     *
     * @return
     */
    @Override
    public EChartsDTO monthTradeTypeReport(BillQueryDTO billQueryDTO) {
        EChartsDTO eChartsDTO = new EChartsDTO();
        Map<String,Object> queryParam = new HashMap<>();
        queryParam.put("mainLabelList", billQueryDTO.getMainLabelList());
        queryParam.put("tradeType", "支出");
        List<MonthDataDTO> monthData = billDetailDao.queryMonthData(queryParam);
        //x轴
        List<String> xAxis = monthData.stream().map(MonthDataDTO::getTradeMonth).distinct().sorted(String::compareTo).collect(Collectors.toList());
        List<String> dataTitle = new ArrayList<>();
        List<Object> totalList = new ArrayList<>();
        Map<String, List<MonthDataDTO>> tradeTypeMap = monthData.stream().collect(Collectors.groupingBy(MonthDataDTO::getTradeType));
        for (Map.Entry<String, List<MonthDataDTO>> tradeTypeEntry : tradeTypeMap.entrySet()) {
            List<MonthDataDTO> tradeDataList = tradeTypeEntry.getValue();
            Map<String, MonthDataDTO> monthDataMap = tradeDataList.stream().collect(Collectors.toMap(MonthDataDTO::getTradeMonth, e1 -> e1, (e1, e2) -> e1));
            dataTitle.add(tradeTypeEntry.getKey());
            List<String> tradeType1 = new ArrayList<>();
            totalList.add(tradeType1);
            for (String date : xAxis) {
                MonthDataDTO monthDataDTO = monthDataMap.get(date);
                if(ObjectUtils.isEmpty(monthDataDTO)){
                    tradeType1.add("0.00");
                }else {
                    tradeType1.add(monthDataDTO.getTradeSum());
                }
            }
        }
        eChartsDTO.setXAxis(xAxis);
        eChartsDTO.setYAxis(totalList);
        eChartsDTO.setDataTitle(dataTitle);
        return eChartsDTO;
    }

    /**
     * 查询账单数据
     *
     * @param baseDTO
     * @return
     */
    @Override
    public CommonPageDTO queryBillList(BillQueryDTO baseDTO) {
        PageHelper.startPage(baseDTO.getPageNum(), baseDTO.getPageSize());
        List<BillDetailDTO> query = billDetailDao.queryList(baseDTO);
        PageInfo<BillDetailDTO> pageInfo = new PageInfo<>(query);
        return new CommonPageDTO(pageInfo);
    }

    /**
     * 插入标签
     *
     * @param baseDTO
     * @return
     */
    @Override
    public int addBillLabel(BillDetailDTO baseDTO) {
        return billDetailDao.updateBillLabel(baseDTO);
    }

    /**
     * 查看每个月收支情况
     *
     * @param billQueryDTO
     * @return
     */
    @Override
    public EChartsDTO monthTradeData(BillQueryDTO billQueryDTO) {
        EChartsDTO eChartsDTO = new EChartsDTO();
        Map<String,Object> queryParam = new HashMap<>();
        queryParam.put("month",billQueryDTO.getMonth());
        List<MonthDataDTO> incomeDataList = billDetailDao.queryMonthData(queryParam);
        eChartsDTO.setData(Collections.singletonList(incomeDataList));
        return eChartsDTO;
    }


    public Date changeDateTo2019(Date date) {
        LocalDate date1 = LocalDate.of(2019, date.getMonth() + 1, date.getDate());
        ZoneId zone = ZoneId.of("Asia/Shanghai");
        Instant instant = date1.atStartOfDay().atZone(zone).toInstant();
        Date date2 = Date.from(instant);
        return date2;
    }

    public String dataSourceChange(String dataSource) {
        switch (dataSource) {
            case "微信":
                return "1";
            case "支付宝":
                return "2";
            case "银行卡":
                return "3";
        }
        return "";
    }

    public String tradeClassChange(String tradeClass) {
        switch (tradeClass) {
            case "餐饮":
                return "1";
            case "聚会":
                return "2";
            case "发红包":
                return "8";
            case "生活必须":
                return "3";
            case "买菜":
                return "2";
            case "游戏充值":
                return "11";
            case "人情":
                return "13";
            case "房租":
                return "6";
            case "交通":
                return "4";
            case "借款":
                return "14";
            case "红包":
                return "16";
            case "奖金":
                return "17";
            case "还款":
                return "18";
            case "理发":
                return "10";
        }
        return "";
    }


}
