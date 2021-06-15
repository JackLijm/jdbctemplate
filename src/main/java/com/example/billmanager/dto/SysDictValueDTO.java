package com.example.billmanager.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * sys_dict_key
 * @author 
 */
@Data
public class SysDictValueDTO implements Serializable {
    /**
     * 字典类型
     */
    private String dictKey;

    /**
     * 字典值
     */
    private String dictValue;

    /**
     * 类型名称
     */
    private String valueName;

    private static final long serialVersionUID = 1L;

    public String tableKey(){
        return dictKey + "\r\n" + dictValue;
    }
}