package com.example.jdbcteplate.trade.service.bill;

import com.csvreader.CsvReader;
import com.example.jdbcteplate.dao.BillDetailDao;
import com.example.jdbcteplate.dto.BillDetailDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * 账单解析
 */
@Component
public class BillFileAnalysis {
    private Logger log = LoggerFactory.getLogger(BillFileAnalysis.class);

    @Autowired
    BillDetailDao billDetailDao;
    @Autowired
    BillAnalysisFactory billAnalysisFactory;

    @Transactional(rollbackFor = Exception.class)
    public void importBill(String filePath, String billType) throws IOException, ParseException {
        BillAnalysisService billAnalysisService = billAnalysisFactory.getInstance(billType);
        log.info("导入文件{},使用解析器{}", filePath, billAnalysisService);
        try {
            //初始化文件流
            billAnalysisService.initReader(filePath);
            //读走文件头
            billAnalysisService.readHead();
            //解析文件
            List<BillDetailDTO> billDetailDTOS = billAnalysisService.readContent();
            //落库
            for (BillDetailDTO billDetailDTO : billDetailDTOS) {
                billDetailDao.insert(billDetailDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            billAnalysisService.closeReader();
        }
    }

}
