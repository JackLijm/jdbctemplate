package com.example.billmanager.dao;

import com.example.billmanager.dto.BillFileRecordDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BillFileRecordDao {
    int deleteByPrimaryKey(Integer billFileId);

    int insert(BillFileRecordDTO record);

    int insertSelective(BillFileRecordDTO record);

    BillFileRecordDTO selectByPrimaryKey(Integer billFileId);

    int updateByPrimaryKeySelective(BillFileRecordDTO record);

    int updateByPrimaryKey(BillFileRecordDTO record);
}