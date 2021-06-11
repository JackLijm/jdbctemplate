package com.example.billmanager.trade.service.bill;

import com.example.billmanager.common.CommonState;
import com.example.billmanager.dao.BillDetailDao;
import com.example.billmanager.dao.BillFileRecordDao;
import com.example.billmanager.dto.BillDetailDTO;
import com.example.billmanager.dto.BillFileRecordDTO;
import com.example.billmanager.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.ParseException;
import java.util.Date;
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
    BillFileRecordDao billFileRecordDao;
    @Autowired
    BillAnalysisFactory billAnalysisFactory;
    @Autowired
    UserInfoService userInfoService;

    @Transactional(rollbackFor = Exception.class)
    public void importBill(String fileName, String filePath, String billType) throws IOException, ParseException {
        BillAnalysisService billAnalysisService = billAnalysisFactory.getInstance(billType);
        log.info("导入文件{},使用解析器{}", filePath, billAnalysisService);
        String batchNo = billAnalysisService.generatorBatchNo();
        String userCode= userInfoService.getUserCode();
        try {
            //初始化文件流
            billAnalysisService.initReader(filePath);
            //读走文件头
            billAnalysisService.readHead();
            //解析文件
            List<BillDetailDTO> billDetailDTOS = billAnalysisService.readContent(batchNo);
            //落库
            for (BillDetailDTO billDetailDTO : billDetailDTOS) {
                billDetailDTO.setAccountNo(userCode);
                billDetailDao.insert(billDetailDTO);
            }
            //插入记录
            BillFileRecordDTO fileRecordDTO = new BillFileRecordDTO();
            fileRecordDTO.setImportBatchNo(batchNo);
            fileRecordDTO.setFileName(fileName);
            fileRecordDTO.setImportDate(new Date());
            fileRecordDTO.setImportStatus(CommonState.FILE_IMPORT_STATUS_SUCCESS_1);
            fileRecordDTO.setImportUser(userCode);
            billFileRecordDao.insert(fileRecordDTO);
        } catch (Exception e) {
            //插入导入失败记录
            BillFileRecordDTO fileRecordDTO = new BillFileRecordDTO();
            fileRecordDTO.setImportBatchNo(batchNo);
            fileRecordDTO.setFileName(fileName);
            fileRecordDTO.setImportDate(new Date());
            fileRecordDTO.setImportStatus(CommonState.FILE_IMPORT_STATUS_ERROR_0);
            fileRecordDTO.setImportUser(userCode);
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            String errorMsg = writer.toString();
            errorMsg = errorMsg.length()>256?errorMsg.substring(0,256):errorMsg;
            fileRecordDTO.setImportResult(errorMsg);
            billFileRecordDao.insert(fileRecordDTO);
            e.printStackTrace();
            throw e;
        } finally {
            billAnalysisService.closeReader();
        }
    }

}
