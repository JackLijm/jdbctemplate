package com.example.billmanager.dao;

import  com.example.billmanager.brickyard.dto.BookingInfoDto;
import com.example.billmanager.brickyard.dto.QueryParamDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookingInfoDao {

    int insertBooingInfo(BookingInfoDto record);

    List<BookingInfoDto> queryBookingInfoList(QueryParamDto queryParamMap);

    int deleteBookingInfo(QueryParamDto queryParamDto);

    int updateBookingInfo(BookingInfoDto bookingInfoDto);
}