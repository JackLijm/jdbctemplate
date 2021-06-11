package com.example.billmanager.trade.controller;

import com.example.billmanager.dto.*;
import com.example.billmanager.service.UserInfoService;
import com.example.billmanager.trade.service.TradeService;
import com.example.billmanager.trade.service.bill.BillFileAnalysis;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.ParseException;

@RestController
@RequestMapping("/trade")
public class TradeController {
    @Autowired
    TradeService tradeService;
    @Autowired
    BillFileAnalysis billFileAnalysis;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    UserInfoService userInfoService;

    /**
     * 插入交易记录
     *
     * @param tradeDetailDTO
     * @return
     */
    @RequestMapping("/add")
    public CommonDTO addTradeDetail(@RequestBody TradeDetailDTO tradeDetailDTO) {
        tradeService.addTradeDetail(tradeDetailDTO);
        return CommonDTO.ok();
    }

    /**
     * 修改交易记录
     *
     * @param tradeDetailDTO
     * @return
     */
    @RequestMapping("/update")
    public CommonDTO updateTradeDetail(@RequestBody TradeDetailDTO tradeDetailDTO) {
        tradeService.updateTradeDetail(tradeDetailDTO);
        return CommonDTO.ok();
    }


    /**
     * 删除交易记录
     *
     * @param tradeDetailDTO
     * @return
     */
    @RequestMapping("/del")
    public CommonDTO delTradeDetail(@RequestBody TradeDetailDTO tradeDetailDTO) {
        tradeService.delTradeDetail(tradeDetailDTO);
        return CommonDTO.ok();
    }

    /**
     * 查询交易记录
     *
     * @param baseDTO
     * @return
     */
    @RequestMapping("/list")
    public CommonDTO queryTradeDetail(@RequestBody BaseDTO baseDTO) {
        CommonDTO result = CommonDTO.ok();
        CommonPageDTO commonPageDTO = tradeService.queryTradeDetail(baseDTO);
        result.setData(commonPageDTO);
        return result;
    }

    /**
     * 增加标签
     *
     * @param baseDTO
     * @return
     */
    @RequestMapping("/addBillLabel")
    public CommonDTO addBillLabel(@RequestBody BillDetailDTO baseDTO) {
        int count = tradeService.addBillLabel(baseDTO);
        return CommonDTO.ok();
    }

    /**
     * 查询交易记录
     *
     * @param baseDTO
     * @return
     */
    @RequestMapping("/billList")
    public CommonDTO queryBillList(@RequestBody BillQueryDTO baseDTO) {
        CommonDTO result = CommonDTO.ok();
        CommonPageDTO commonPageDTO = tradeService.queryBillList(baseDTO);
        result.setData(commonPageDTO);
        return result;
    }


    /**
     * 导入excel
     *
     * @param baseDTO
     * @return
     */
    @RequestMapping("/importExcel")
    public CommonDTO tempImportExcel(@RequestBody(required = false) BaseDTO baseDTO) throws IOException {
        tradeService.tempImportExcel(baseDTO);
        return CommonDTO.ok();
    }


    /**
     * 查看月份收入-支出报表
     *
     * @return
     */
    @RequestMapping("/tradeTypeReport")
    public CommonDTO tradeTypeReport() {
        CommonDTO result = CommonDTO.ok();
        EChartsDTO eChartsDTO = tradeService.tradeTypeReport();
        result.setData(eChartsDTO);
        return result;
    }

    /**
     * 查看月份分类报表
     *
     * @return
     */
    @RequestMapping("/monthTradeTypeReport")
    public CommonDTO monthTradeTypeReport(@RequestBody(required = false) BillQueryDTO billQueryDTO) {
        CommonDTO result = CommonDTO.ok();
        EChartsDTO eChartsDTO = tradeService.monthTradeTypeReport(billQueryDTO);
        result.setData(eChartsDTO);
        return result;
    }

    /**
     * 查看每个月收支情况
     *
     * @return
     */
    @RequestMapping("/monthTradeData")
    public CommonDTO monthTradeData(@RequestBody(required = false) BillQueryDTO billQueryDTO) {
        CommonDTO result = CommonDTO.ok();
        EChartsDTO eChartsDTO = tradeService.monthTradeData(billQueryDTO);
        result.setData(eChartsDTO);
        return result;
    }

    /**
     * 导入对账单文件
     *
     * @return
     */
    @RequestMapping("/importBillFile")
    public CommonDTO importBillFile(@RequestParam("file") MultipartFile file, @RequestParam("billType") String billType) throws IOException, ParseException {
        //getName() 方法 返回参数的名称 这里返回的也就是 file
        String fileName = file.getOriginalFilename();
        String path = request.getSession().getServletContext().getRealPath("/") + fileName;
        //这里我试了，可以用 fileName 也可以用 OriginalFilename 都没问题的
        File f = new File(path);          //transferTo() 我主要就是用来把 MultipartFile 转换成 File
        try (InputStream inputStream = file.getInputStream(); OutputStream out = new FileOutputStream(f)) {
            IOUtils.copy(inputStream, out);
        }
        billFileAnalysis.importBill(fileName, f.getAbsolutePath(), billType);
        return CommonDTO.ok();
    }


}
