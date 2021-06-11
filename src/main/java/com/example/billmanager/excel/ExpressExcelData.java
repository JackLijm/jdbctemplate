package com.example.billmanager.excel;

import com.example.billmanager.dto.TradeDetailDTO;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * 解析excel的数据
 */
public class ExpressExcelData {

    public static void main(String[] args) throws IOException {
        //1.读取xlsx文件
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream("C:\\Users\\Administrator\\Desktop\\template.xlsx"));
        //2.获取sheet页
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(1);
        for (int i = 0; i <= xssfSheet.getLastRowNum(); i++) {
            //3.读每一行，
            XSSFRow xssfRow = xssfSheet.getRow(i);//获取每一行的数据
            Date dateCellValue = xssfRow.getCell(0).getDateCellValue();
            String tradeClass = xssfRow.getCell(1).getStringCellValue();
            double tradeSum = xssfRow.getCell(5).getNumericCellValue();
            String dataSource = xssfRow.getCell(3).getStringCellValue();
            String remark = xssfRow.getCell(4)==null?"":xssfRow.getCell(4).getStringCellValue();
            TradeDetailDTO tradeDetailDTO = new TradeDetailDTO();
            tradeDetailDTO.setTradeDate(changeDateTo2019(dateCellValue));
            tradeDetailDTO.setDataSource(dataSourceChange(dataSource));
            tradeDetailDTO.setRemark(remark);
            tradeDetailDTO.setTradeClass(tradeClassChange(tradeClass));
            tradeDetailDTO.setTradeSum(new BigDecimal(tradeSum));
            tradeDetailDTO.setTradeType("2");
        }
    }

    public static Date changeDateTo2019(Date date){
        LocalDate date1 = LocalDate.of(2019,date.getMonth()+1,date.getDate());
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = date1.atStartOfDay().atZone(zone).toInstant();
        Date date2 = Date.from(instant);
        return date2;
    }

    public static String dataSourceChange(String dataSource){
        switch (dataSource){
            case "微信":
                return "1";
            case "支付宝":
                return "2";
            case "银行卡":
                return "3";
        }
        return "";
    }
    public static String tradeClassChange(String tradeClass){
        switch (tradeClass){
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
            case "理发":
                return "10";
        }
        return "";
    }
}
