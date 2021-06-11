package com.example.billmanager.trade.service.bill;

import com.csvreader.CsvReader;
import com.example.billmanager.common.SerialNoGenerateUtil;
import com.example.billmanager.dto.BillDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component("cmbBillAnalysis")
@Scope("prototype")
public class CMBBillAnalysis implements BillAnalysisService {
    @Autowired
    private SerialNoGenerateUtil serialNoGenerateUtil;
    CsvReader r = null;
    /**
     * 读取文件头
     *
     * @param filePath
     */
    @Override
    public void initReader(String filePath) throws FileNotFoundException {
        r = new CsvReader(filePath, ',', Charset.forName("gbk"));
    }

    /**
     * 读取文件头
     */
    @Override
    public void readHead() throws IOException {
        //逐条读取记录，直至读完
        while (r.readRecord()) {
            if ("交易日期".equals(r.get(0).trim())) {
                break;
            }
        }
    }

    /**
     * 读取文件内容
     */
    @Override
    public List<BillDetailDTO> readContent(String batchNo) throws IOException, ParseException {
        List<BillDetailDTO> billDetailDTOS = new ArrayList<>();
        String tradeType = "";
        String tradeSum = "";
        while (r.readRecord()) {
            System.out.println(Arrays.toString(r.getValues()));
            if (r.get(0).trim().contains("#")) {
                break;
            }
            BillDetailDTO billDetailDTO = new BillDetailDTO();

            billDetailDTO.setSerialNo(serialNoGenerateUtil.getSerialNo());
            billDetailDTO.setTradeTime(TIME_FORMATE3.parse(r.get(0).replace("\t","")+ " " + r.get(1).replace("\t","")));
            if (StringUtils.isEmpty(r.get(2).trim())) {
                tradeType = "支出";
                tradeSum = r.get(3);
            }else {
                tradeType = "收入";
                tradeSum = r.get(2);
            }
            billDetailDTO.setTradeSum(new BigDecimal(tradeSum));
            billDetailDTO.setTradeType(tradeType);
            billDetailDTO.setRemark(r.get(6));
            billDetailDTO.setDataSource("招商银行");
            billDetailDTO.setTradeStatus("");
            billDetailDTO.setPayType(r.get(5));
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

    private String formateDate(String date,String time){
        String year = date.substring(0,3);
        String month = date.substring(4,5);
        String day = date.substring(5,6);
        return date +time;
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
