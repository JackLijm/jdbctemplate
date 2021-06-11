package com.example.billmanager.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * bill_file_record
 * @author 
 */
@Data
public class BillFileRecordDTO implements Serializable {
    /**
     * 主键
     */
    private Integer billFileId;

    /**
     * 导入明细批次号
     */
    private String importBatchNo;

    /**
     * 导入文件名称
     */
    private String fileName;

    /**
     * 导入状态，1-导入成功 0-导入失败
     */
    private String importStatus;

    /**
     * 导入时间
     */
    private Date importDate;

    /**
     * 导入结果说明
     */
    private String importResult;

    /**
     * 上传用户
     */
    private String importUser;

    private static final long serialVersionUID = 1L;
}