package com.example.billmanager.trade.service.bill;

import com.example.billmanager.common.SerialNoGenerateUtil;
import com.example.billmanager.dto.BillDetailDTO;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component("pabcBillAnalysis")
@Scope("prototype")
public class PABCBillAnalysis implements BillAnalysisService {
    private HSSFWorkbook workbook = null;
    private Iterator<Row> rowIterator = null;
    @Autowired
    private SerialNoGenerateUtil serialNoGenerateUtil;

    /**
     * 读取文件头
     *
     * @param filePath
     */
    @Override
    public void initReader(String filePath) throws IOException {
        File file = new File(filePath);
        workbook = new HSSFWorkbook(new FileInputStream(file));
        HSSFSheet sheetAt = workbook.getSheetAt(0);
        rowIterator = sheetAt.iterator();
    }

    /**
     * 读取文件头
     */
    @Override
    public void readHead() throws IOException {
        while (rowIterator.hasNext()) {
            Row next = rowIterator.next();
            if ("交易时间".equals(next.getCell(0).getStringCellValue())) {
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
        while (rowIterator.hasNext()) {
            Row next = rowIterator.next();
            BillDetailDTO billDetailDTO = new BillDetailDTO();
            billDetailDTO.setSerialNo(serialNoGenerateUtil.getSerialNo());

            billDetailDTO.setTradeNo(getCellStringValue(next, 8));
            billDetailDTO.setTradeTime(TIME_FORMATE.parse(getCellStringValue(next, 0)));
            billDetailDTO.setTradeTo(getCellStringValue(next, 1));
            billDetailDTO.setGoodsName("");
            billDetailDTO.setTradeSum(new BigDecimal(getCellStringValue(next, 4)));
            billDetailDTO.setTradeType(translateTradeType(getCellStringValue(next, 3)));
            billDetailDTO.setRemark(getCellStringValue(next, 7));
            billDetailDTO.setDataSource("平安银行");
            billDetailDTO.setTradeStatus(getCellStringValue(next, 6));
            billDetailDTO.setPayType(getCellStringValue(next, 6));
            billDetailDTO.setImportBatch(batchNo);
            billDetailDTOS.add(billDetailDTO);
        }
        return billDetailDTOS;
    }

    private String translateTradeType(String tradeType) {
        String value = "";
        switch (tradeType) {
            case "转入":
                value = "收入";
                break;
            case "转出":
                value = "支出";
                break;
            default:
                value = "";
        }
        return value;
    }

    private String getCellStringValue(Row row, int index) {
        return row.getCell(index).getStringCellValue();
    }

    private Date getDateCellValue(Row row, int index) {
        return row.getCell(index).getDateCellValue();
    }

    private Double getNumericCellValue(Row row, int index) {
        return row.getCell(index).getNumericCellValue();
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
        try {
            if (workbook != null) {
                workbook.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
