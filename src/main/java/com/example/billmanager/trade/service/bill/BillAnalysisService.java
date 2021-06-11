package com.example.billmanager.trade.service.bill;

import com.example.billmanager.dto.BillDetailDTO;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public interface BillAnalysisService {
    public static DateFormat TIME_FORMATE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static DateFormat TIME_FORMATE3 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    public static DateFormat TIME_FORMATE2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    public static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("￥###,##");
    /**
     * 读取文件头
     */
    public void initReader(String filePath) throws IOException;

    /**
     * 生成批次号
     * @return
     */
    default String generatorBatchNo() {
        return TIME_FORMATE2.format(new Date());
    }


    /**
     * 读取文件头
     */
    public void readHead() throws IOException;

    /**
     * 读取文件内容
     */
    public List<BillDetailDTO> readContent(String batchNo) throws IOException, ParseException;

    /**
     * 读取文件文件尾部
     */
    public void readTail();

    /**
     * 关闭文件流
     */
    public void closeReader();

    /**
     * 获取总记录数
     * @return
     */
    public long contentLength() throws IOException;
}
