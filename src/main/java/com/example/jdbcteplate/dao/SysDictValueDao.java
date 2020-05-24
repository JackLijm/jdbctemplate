package com.example.jdbcteplate.dao;

import com.example.jdbcteplate.dto.SysDictValueDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysDictValueDao {
    int insert(SysDictValueDTO record);

    List<SysDictValueDTO> select();

    int insertSelective(SysDictValueDTO record);

    int update(SysDictValueDTO record);

    int delete(SysDictValueDTO record);
}