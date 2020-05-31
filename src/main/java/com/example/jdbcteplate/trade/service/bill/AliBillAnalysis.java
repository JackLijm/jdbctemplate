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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component("aliBillAnalysis")
@Scope("prototype")
public class AliBillAnalysis implements BillAnalysisService {
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
            if ("交易号".equals(r.get(0).trim())) {
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
            if ("------------------------------------------------------------------------------------".equals(r.get(0).trim())) {
                break;
            }
            BillDetailDTO billDetailDTO = new BillDetailDTO();
            billDetailDTO.setAccountNo("1027400380@qq.com");
            billDetailDTO.setTradeNo(r.get(0));
            billDetailDTO.setSerialNo(serialNoGenerateUtil.getSerialNo());
            System.out.println(r.get(4));
            billDetailDTO.setTradeTime(TIME_FORMATE.parse(r.get(4)));
            billDetailDTO.setTradeTo(r.get(7));
            billDetailDTO.setGoodsName(r.get(8));
            billDetailDTO.setTradeSum(new BigDecimal(r.get(9)));
            billDetailDTO.setTradeType(r.get(15).replace("已", ""));
            billDetailDTO.setRemark(r.get(14));
            billDetailDTO.setDataSource("支付宝");
            billDetailDTO.setTradeTo(r.get(7));
            billDetailDTO.setTradeStatus(r.get(11));
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
        long currentRecord = r.getCurrentRecord();
        while (r.readRecord()) {
            if ("------------------------------------------------------------------------------------".equals(r.get(0).trim())) {
                break;
            }
        }
        return 0;
    }
}
