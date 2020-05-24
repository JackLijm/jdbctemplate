package com.example.jdbcteplate.dto;

import lombok.Data;

@Data
public class CommonDTO {
    private String code;
    private String msg;
    private Object data;

    public static  CommonDTO ok(){
        CommonDTO commonDTO = new CommonDTO();
        commonDTO.setCode("0000");
        commonDTO.setMsg("处理成功");
        return commonDTO;
    }

    public static  CommonDTO ok(String msg){
        CommonDTO commonDTO = new CommonDTO();
        commonDTO.setCode("0000");
        commonDTO.setMsg(msg);
        return commonDTO;
    }

    public static  CommonDTO err(String msg){
        CommonDTO commonDTO = new CommonDTO();
        commonDTO.setCode("-1");
        commonDTO.setMsg(msg);
        return commonDTO;
    }
}
