package com.example.jdbcteplate.controller.common;

import com.example.jdbcteplate.dao.SysDictKeyDao;
import com.example.jdbcteplate.dao.SysDictValueDao;
import com.example.jdbcteplate.dto.CommonDTO;
import com.example.jdbcteplate.dto.SysDictKeyDTO;
import com.example.jdbcteplate.dto.SysDictValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dict")
public class DictController {
    @Autowired
    SysDictKeyDao dictKeyDao;
    @Autowired
    SysDictValueDao valueDao;

    /**
     * 增加字典类型
     * @param sysDictKeyDTO
     * @return
     */
    @RequestMapping("/addDictKey")
    public CommonDTO addDictKey(@RequestBody SysDictKeyDTO sysDictKeyDTO){
        int insert = dictKeyDao.insert(sysDictKeyDTO);
        return CommonDTO.ok();
    }

    /**
     * 修改字典类型
     * @param sysDictKeyDTO
     * @return
     */
    @RequestMapping("/updateDictKey")
    public CommonDTO updateDictKey(@RequestBody SysDictKeyDTO sysDictKeyDTO){
        int insert = dictKeyDao.update(sysDictKeyDTO);
        return CommonDTO.ok();
    }

    /**
     * 删除字典类型
     * @param sysDictKeyDTO
     * @return
     */
    @RequestMapping("/delDictKey")
    public CommonDTO deleteDictKey(@RequestBody SysDictKeyDTO sysDictKeyDTO){
        int insert = dictKeyDao.delete(sysDictKeyDTO);
        return CommonDTO.ok();
    }

    /**
     * 增加字典值
     * @param sysDictValueDTO
     * @return
     */
    @RequestMapping("/addDictValue")
    public CommonDTO addDictValue(@RequestBody SysDictValueDTO sysDictValueDTO){
        int insert = valueDao.insert(sysDictValueDTO);
        return CommonDTO.ok();
    }

    /**
     * 修改字典值
     * @param sysDictValueDTO
     * @return
     */
    @RequestMapping("/updateDictValue")
    public CommonDTO updateDictValue(@RequestBody SysDictValueDTO sysDictValueDTO){
        int insert = valueDao.update(sysDictValueDTO);
        return CommonDTO.ok();
    }
    /**
     * 删除字典值
     * @param sysDictValueDTO
     * @return
     */
    @RequestMapping("/delDictValue")
    public CommonDTO delDictValue(@RequestBody SysDictValueDTO sysDictValueDTO){
        int insert = valueDao.delete(sysDictValueDTO);
        return CommonDTO.ok();
    }

    /**
     * 查询字典信息
     * @return
     */
    @RequestMapping("/cacheDict")
    public CommonDTO loadCache(){
        CommonDTO commonDTO = CommonDTO.ok();
        List<SysDictKeyDTO> keySelect = dictKeyDao.select();
        List<SysDictValueDTO> valueSelect = valueDao.select();
        Map<String,Object> data = new HashMap<>();
        data.put("dictKey",keySelect);
        data.put("dictValue",valueSelect);
        commonDTO.setData(data);
        return commonDTO;
    }
}
