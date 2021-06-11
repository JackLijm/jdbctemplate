/**
 * <p>文件名称: BrickyardController.java</p>
 * <p>文件描述: </p>
 * <p>版权所有: 版权所有(C)2016-</p>
 * <p>公    司: 金证财富南京科技有限公司</p>
 * <p>内容摘要: </p>
 * <p>其他说明: </p>
 * <p>创建日期： 2021/2/2 13:49 </p>
 * <p>完成日期：</p>
 * <p>修改记录1:</p>
 * <pre>
 *    修改日期：
 *    版 本 号：
 *    修 改 人：
 *    修改内容：
 * </pre>
 * <p>修改记录2：…</p>
 *
 * @version 1.0
 * @author lijm@szkingdom.com
 */
package com.example.billmanager.brickyard.controller;

import com.example.billmanager.brickyard.dto.BookingInfoDto;
import com.example.billmanager.brickyard.dto.PayInfoDto;
import com.example.billmanager.brickyard.dto.QueryParamDto;
import com.example.billmanager.brickyard.entry.EntryBookingInfo;
import com.example.billmanager.brickyard.entry.EntryFactory;
import com.example.billmanager.brickyard.entry.EntryPayInfo;
import com.example.billmanager.brickyard.util.DateUtil;
import com.example.billmanager.common.CommonState;
import com.example.billmanager.dao.BookingInfoDao;
import com.example.billmanager.dao.PayInfoDao;
import com.example.billmanager.dto.CommonDTO;
import com.example.billmanager.dto.CommonPageDTO;
import com.example.billmanager.dto.DtoUtils;
import com.example.billmanager.dto.EChartsDTO;
import com.example.billmanager.exception.BusinessException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/brickyard")
public class BrickyardController {

    @Autowired
    BookingInfoDao bookingInfoDao;

    @Autowired
    PayInfoDao payInfoDao;

    @RequestMapping("/queryBookingInfo")
    public CommonDTO queryBookingInfo(@RequestBody QueryParamDto queryParamDto) {
        PageHelper.startPage(queryParamDto.getPageNum(), queryParamDto.getPageSize());
        List<BookingInfoDto> bookingInfoList = bookingInfoDao.queryBookingInfoList(queryParamDto);
        CommonDTO ok = CommonDTO.ok();
        PageInfo<BookingInfoDto> pageInfo = new PageInfo<>(bookingInfoList);
        ok.setData(new CommonPageDTO(pageInfo));
        return ok;
    }

    @RequestMapping("/insertBookingInfo")
    public CommonDTO insertBookingInfo(@RequestBody BookingInfoDto bookingInfoDto) {
        bookingInfoDto.setBookingId(getId());
        DtoUtils.setCommonInfo(bookingInfoDto, "system");
        bookingInfoDao.insertBooingInfo(bookingInfoDto);
        return CommonDTO.ok();
    }

    @RequestMapping("/insertPayInfo")
    @Transactional(rollbackFor = Exception.class)
    public CommonDTO insertPayInfo(@RequestBody PayInfoDto queryParamDto) throws BusinessException {
        DtoUtils.setCommonInfo(queryParamDto, "system");
        queryParamDto.setPaySerialNo(getId());
        int count = payInfoDao.insertPayInfo(queryParamDto);

        //已结清订单不可以修改结清表示
        checkClearFlag(queryParamDto.getBookingId(), queryParamDto.getIsClear());

        if (!StringUtils.isEmpty(queryParamDto.getIsClear())) {
            BookingInfoDto bookingInfoDto = new BookingInfoDto();
            bookingInfoDto.setBookingId(queryParamDto.getBookingId());
            bookingInfoDto.setIsClear(queryParamDto.getIsClear());
            count = bookingInfoDao.updateBookingInfo(bookingInfoDto);
        }
        return CommonDTO.ok();
    }

    private void checkClearFlag(String bookingId, String isClear) throws BusinessException {

        QueryParamDto queryParamDto = new QueryParamDto();
        queryParamDto.setBookingId(bookingId);
        List<BookingInfoDto> bookingInfoList = bookingInfoDao.queryBookingInfoList(queryParamDto);
        BookingInfoDto bookingInfoDto1 = bookingInfoList.get(0);
        if ("1".equals(bookingInfoDto1.getIsClear())) {
            throw new BusinessException("9999", "订单已结清，不可修改标识");
        }

    }

    @RequestMapping("/queryPayDetail")
    public CommonDTO queryPayDetail(@RequestBody QueryParamDto queryParamDto) {
        List<PayInfoDto> payInfoList = payInfoDao.queryPayInfoList(queryParamDto);
        CommonDTO ok = CommonDTO.ok();
        ok.setData(payInfoList);
        return ok;
    }

    @RequestMapping("/batchClear")
    @Transactional(rollbackFor = Exception.class)
    public CommonDTO batchClear(@RequestBody List<PayInfoDto> payInfoDtoList) throws BusinessException {
        EntryFactory nEntryFactory = new EntryFactory();
        EntryPayInfo nEntryPayInfo = nEntryFactory.getEntry(EntryPayInfo.class);
        EntryBookingInfo nEntryBookingInfo = nEntryFactory.getEntry(EntryBookingInfo.class);

        for (PayInfoDto payInfoDto : payInfoDtoList) {
            checkClearFlag(payInfoDto.getBookingId(), "1");

            if (ObjectUtils.isEmpty(payInfoDto.getPaySum())) {
                throw new BusinessException("9998", "付款金额未输入");
            }

            payInfoDto.setPaySerialNo(getId());
            payInfoDto.setIsClear("1");
            payInfoDto.setPayDate(new Date());
            payInfoDto.setPayAmount(payInfoDto.getPayAmount());
            DtoUtils.setCommonInfo(payInfoDto, "system");
            //插入一条支付信息
            nEntryPayInfo.addPayInfo(payInfoDto);
            //更新订单结清标识
            BookingInfoDto bookingInfoDto = new BookingInfoDto();
            DtoUtils.setCommonInfo(bookingInfoDto, "system");
            bookingInfoDto.setOperateType(CommonState.OPERATOR_TYPE_UPDATE);
            bookingInfoDto.setBookingId(payInfoDto.getBookingId());
            bookingInfoDto.setIsClear("1");
            nEntryBookingInfo.addBookingInfo(bookingInfoDto);

        }
        nEntryPayInfo.insertDb(payInfoDao);
        nEntryBookingInfo.updateDb(bookingInfoDao);
        CommonDTO ok = CommonDTO.ok();
        return ok;
    }

    @RequestMapping("/delBookingInfo")
    public CommonDTO delBookingInfo(@RequestBody QueryParamDto queryParamDto) {
        int count = payInfoDao.deletePayInfo(queryParamDto);
        count = bookingInfoDao.deleteBookingInfo(queryParamDto);
        CommonDTO ok = CommonDTO.ok();
        return ok;
    }

    @RequestMapping("/queryBrickyardReport")
    public CommonDTO queryBrickyardReport(@RequestBody QueryParamDto queryParamDto) {
        //查询订单信息
        List<BookingInfoDto> bookingInfoList = bookingInfoDao.queryBookingInfoList(queryParamDto);
        List<PayInfoDto> payInfoDtoList = payInfoDao.queryPayInfoList(queryParamDto);
        Map<String, List<BookingInfoDto>> groupMap = new HashMap<>();
        //按照查询条件分组
        //按年
        if ("3".equals(queryParamDto.getTradeMode())) {
            groupMap = bookingInfoList.stream().collect(Collectors.groupingBy(e -> DateUtil.YEAR_FORMATE.format(e.getApplyDate())));
        } else if ("2".equals(queryParamDto.getTradeMode())) {
            //按季度
            groupMap = bookingInfoList.stream().collect(Collectors.groupingBy(e -> DateUtil.getDateQuarter(e.getApplyDate())));
        } else if ("1".equals(queryParamDto.getTradeMode())) {
            //按月
            groupMap = bookingInfoList.stream().collect(Collectors.groupingBy(e -> DateUtil.MONTH_FORMATE_1.format(e.getApplyDate())));
        }
        else if ("4".equals(queryParamDto.getTradeMode())) {
            //按天
            groupMap = bookingInfoList.stream().collect(Collectors.groupingBy(e -> DateUtil.DATE_FORMATE.format(e.getApplyDate())));
        }

        //销售数量
        EChartsDTO sellCountInfo = getSellCountInfo(groupMap, bookingInfoList);
        //销售价格
        EChartsDTO sellMoneyInfo = getSellMoneyInfo(groupMap, bookingInfoList);


        //总营业额
        BigDecimal sumAmount = bookingInfoList.stream().map(e -> e.getTradeCount().multiply(e.getTradeUnit())).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        //已结清营业额
        BigDecimal alreadyPayAmount = bookingInfoList.stream().filter(e -> "1".equals(e.getIsClear())).map(e -> e.getTradeCount().multiply(e.getTradeUnit())).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        //未结清
        BigDecimal unPayAmount = bookingInfoList.stream().filter(e -> !"1".equals(e.getIsClear())).map(e -> e.getTradeCount().multiply(e.getTradeUnit())).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        //已收款
        BigDecimal actualPayAmount = payInfoDtoList.stream().filter(e->!ObjectUtils.isEmpty(e.getPayAmount())).map(PayInfoDto::getPayAmount).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);


        CommonDTO ok = CommonDTO.ok();
        Map<String, Object> returnData = new HashMap<>();
        returnData.put("sumAmount", sumAmount);
        returnData.put("alreadyPayAmount", alreadyPayAmount);
        returnData.put("unPayAmount", unPayAmount);
        returnData.put("actualPayAmount", actualPayAmount);
        returnData.put("sellCountInfo", sellCountInfo);
        returnData.put("sellMoneyInfo",sellMoneyInfo);
        ok.setData(returnData);
        return ok;
    }

    private EChartsDTO getSellMoneyInfo(Map<String, List<BookingInfoDto>> groupMap, List<BookingInfoDto> bookingInfoList) {
        EChartsDTO eChartsDTO = new EChartsDTO();
        //x轴
        List<String> xAxis = groupMap.keySet().stream().distinct().sorted(String::compareTo).collect(Collectors.toList());
        Map<String, List<BigDecimal>> dataMap = new HashMap<>();

        //+汇总的
        List<BigDecimal> totalSumList = new ArrayList<>();
        //y轴
        for (String xAxi : xAxis) {
            List<BookingInfoDto> groupBookingInfoList = groupMap.get(xAxi);
            BigDecimal bigDecimal = groupBookingInfoList.stream().map(e->e.getTradeCount().multiply(e.getTradeUnit())).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            totalSumList.add(bigDecimal);
        }
        dataMap.put("total", totalSumList);
        eChartsDTO.setXAxis(xAxis);
        eChartsDTO.setYAxis(new ArrayList<>(dataMap.values()));
        BigDecimal max = totalSumList.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        eChartsDTO.setMax(max);
        eChartsDTO.setDataTitle(new ArrayList<>(dataMap.keySet()));
        return eChartsDTO;
    }

    private EChartsDTO getSellCountInfo(Map<String, List<BookingInfoDto>> groupMap, List<BookingInfoDto> bookingInfoList) {
        EChartsDTO eChartsDTO = new EChartsDTO();
        //x轴
        List<String> xAxis = groupMap.keySet().stream().distinct().sorted(String::compareTo).collect(Collectors.toList());
        List<String> brickTypeList = bookingInfoList.stream().map(BookingInfoDto::getBrickType).distinct().sorted(String::compareTo).collect(Collectors.toList());

        Map<String, List<BigDecimal>> dataMap = new HashMap<>();
        for (String brickType : brickTypeList) {
            dataMap.put(brickType, new ArrayList<>());
        }

        //+汇总的
        List<BigDecimal> totalSumList = new ArrayList<>();
        //y轴
        for (String xAxi : xAxis) {
            List<BookingInfoDto> groupBookingInfoList = groupMap.get(xAxi);
            BigDecimal totalSum = BigDecimal.ZERO;
            for (String brickType : brickTypeList) {
                BigDecimal bigDecimal = groupBookingInfoList.stream().filter(e -> brickType.equals(e.getBrickType())).map(BookingInfoDto::getTradeCount).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
                List<BigDecimal> dataList = dataMap.get(brickType);
                dataList.add(bigDecimal);
                totalSum = totalSum.add(bigDecimal);
            }
            totalSumList.add(totalSum);
        }
        dataMap.put("total", totalSumList);
        eChartsDTO.setXAxis(xAxis);
        eChartsDTO.setYAxis(new ArrayList<>(dataMap.values()));
        BigDecimal max = totalSumList.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        eChartsDTO.setMax(max);
        eChartsDTO.setDataTitle(new ArrayList<>(dataMap.keySet()));
        return eChartsDTO;
    }

    public static String getId() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

}
