package com.example.jdbcteplate.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * sys_dict_key
 * @author 
 */
@Data
public class SysDictKeyDTO implements Serializable {
    /**
     * 字典类型
     */
    private String dictKey;

    /**
     * 类型名称
     */
    private String keyName;

    private static final long serialVersionUID = 1L;
}