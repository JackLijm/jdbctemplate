package com.example.jdbcteplate.dao;

import com.example.jdbcteplate.dto.SysDictKeyDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface SysDictKeyDao {
    int insert(SysDictKeyDTO record);

    List<SysDictKeyDTO> select();

    int insertSelective(SysDictKeyDTO record);

    int update(SysDictKeyDTO record);

    int delete(SysDictKeyDTO record);
}