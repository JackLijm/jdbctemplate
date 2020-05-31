package com.example.jdbcteplate.dto;

import com.github.pagehelper.PageInfo;
import lombok.Data;

@Data
public class CommonPageDTO {
    private long total;
    private int cur;
    private Object tableData;

    public CommonPageDTO() {
    }
    public CommonPageDTO(PageInfo pageInfo) {
        this.total = pageInfo.getTotal();
        this.tableData = pageInfo.getList();
    }
}
