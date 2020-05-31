package com.example.jdbcteplate.trade.service.bill;

import com.example.jdbcteplate.dto.BillDetailDTO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public interface BillAnalysisService {
    public static DateFormat TIME_FORMATE = new SimpleDateFormat("yyyy-MM-dd HH24:mm:ss");
    public static DateFormat TIME_FORMATE3 = new SimpleDateFormat("yyyyMMdd HH24:mm:ss");
    public static DateFormat TIME_FORMATE2 = new SimpleDateFormat("yyyyMMddHH24mmssSSS");
    public static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("￥###,##");
    /**
     * 读取文件头
     */
    public void initReader(String filePath) throws IOException;


    /**
     * 读取文件头
     */
    public void readHead() throws IOException;

    /**
     * 读取文件内容
     */
    public List<BillDetailDTO> readContent() throws IOException, ParseException;

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
