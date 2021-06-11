package com.example.billmanager.controller;

import com.example.billmanager.dto.CommonDTO;
import com.example.billmanager.exception.BusinessException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/money")
public class FontController {
    @RequestMapping(value = "/addConsumerType", produces = {"application/json;charset=UTF-8"}, consumes = {"application/json;charset=UTF-8"})
    public CommonDTO testException(@RequestBody(required = false) Map<String, Object> param) throws BusinessException {
        System.out.println(param);
        return new CommonDTO();
    }
}
