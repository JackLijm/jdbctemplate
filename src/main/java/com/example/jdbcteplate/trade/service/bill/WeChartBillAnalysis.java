package com.example.jdbcteplate.trade.service.bill;

import com.csvreader.CsvReader;
import com.example.jdbcteplate.common.SerialNoGenerateUtil;
import com.example.jdbcteplate.dto.BillDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component("weChartBillAnalysis")
@Scope("prototype")
public class WeChartBillAnalysis implements BillAnalysisService {
    CsvReader r = null;
    @Autowired
    private SerialNoGenerateUtil serialNoGenerateUtil;

    /**
     * 读取文件头
     *
     * @param filePath
     */
    @Override
    public void initReader(String filePath) throws FileNotFoundException {
        r = new CsvReader(filePath, ',', Charset.forName("utf-8"));
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(DECIMAL_FORMAT.parse("￥1,111.00"));
        System.out.println(DECIMAL_FORMAT.parse("￥20.00"));
        System.out.println(DECIMAL_FORMAT.parse("￥0.00"));
    }

    /**
     * 读取文件头
     */
    @Override
    public void readHead() throws IOException {
        //逐条读取记录，直至读完
        while (r.readRecord()) {
            System.out.println(r.getValues());
            if ("交易时间".equals(r.get(0).trim())) {
                break;
            }
        }
    }

    /**
     * 读取文件内容
     */
    @Override
    public List<BillDetailDTO> readContent() throws IOException, ParseException {
        String batchNo = TIME_FORMATE2.format(new Date());
        List<BillDetailDTO> billDetailDTOS = new ArrayList<>();
        while (r.readRecord()) {
            BillDetailDTO billDetailDTO = new BillDetailDTO();
            billDetailDTO.setAccountNo("1027400380@qq.com");
            billDetailDTO.setSerialNo(serialNoGenerateUtil.getSerialNo());
            billDetailDTO.setTradeNo(r.get(8));
            billDetailDTO.setTradeTime(TIME_FORMATE.parse(r.get(0)));
            billDetailDTO.setTradeTo(r.get(2));
            billDetailDTO.setGoodsName(r.get(3));
            billDetailDTO.setTradeSum(new BigDecimal(r.get(5).substring(1).replaceAll(",", "")));
            billDetailDTO.setTradeType(r.get(4));
            billDetailDTO.setRemark(r.get(10));
            billDetailDTO.setDataSource("微信");
            billDetailDTO.setTradeStatus(r.get(7));
            billDetailDTO.setPayType(r.get(6));
            billDetailDTO.setImportBatch(batchNo);
            billDetailDTOS.add(billDetailDTO);
        }
        return billDetailDTOS;
    }

    /**
     * 读取文件文件尾部
     */
    @Override
    public void readTail() {

    }

    /**
     * 关闭文件流
     */
    @Override
    public void closeReader() {
        r.close();
    }

    /**
     * 获取总记录数
     *
     * @return
     */
    @Override
    public long contentLength() throws IOException {
        return 0;
    }
}
