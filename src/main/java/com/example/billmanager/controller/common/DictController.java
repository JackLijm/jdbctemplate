package com.example.billmanager.controller.common;

import com.example.billmanager.cache.DictCache;
import com.example.billmanager.cache.DictValueCache;
import com.example.billmanager.dao.SysDictKeyDao;
import com.example.billmanager.dao.SysDictValueDao;
import com.example.billmanager.dto.CommonDTO;
import com.example.billmanager.dto.SysDictKeyDTO;
import com.example.billmanager.dto.SysDictValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    @Autowired
    DictCache dictCache;
    @Autowired
    DictValueCache dictValueCache;

    String cacheDictValueKey = "dict-value";

    String dictKey = "dict-key";


    /**
     * 增加字典类型
     *
     * @param sysDictKeyDTO
     * @return
     */
    @RequestMapping("/addDictKey")
    public CommonDTO addDictKey(@RequestBody SysDictKeyDTO sysDictKeyDTO) {
        int insert = dictKeyDao.insert(sysDictKeyDTO);
        dictCache.put(dictKey, sysDictKeyDTO.getDictKey(), sysDictKeyDTO);
        return CommonDTO.ok();
    }

    /**
     * 修改字典类型
     *
     * @param sysDictKeyDTO
     * @return
     */
    @RequestMapping("/updateDictKey")
    public CommonDTO updateDictKey(@RequestBody SysDictKeyDTO sysDictKeyDTO) {
        int insert = dictKeyDao.update(sysDictKeyDTO);
        dictCache.put(dictKey, sysDictKeyDTO.getDictKey(), sysDictKeyDTO);
        return CommonDTO.ok();
    }

    /**
     * 删除字典类型
     *
     * @param sysDictKeyDTO
     * @return
     */
    @RequestMapping("/delDictKey")
    public CommonDTO deleteDictKey(@RequestBody SysDictKeyDTO sysDictKeyDTO) {
        int insert = dictKeyDao.delete(sysDictKeyDTO);
        dictCache.remove(dictKey, sysDictKeyDTO.getDictKey());
        return CommonDTO.ok();
    }

    /**
     * 增加字典值
     *
     * @param sysDictValueDTO
     * @return
     */
    @RequestMapping("/addDictValue")
    public CommonDTO addDictValue(@RequestBody SysDictValueDTO sysDictValueDTO) {
        int insert = valueDao.insert(sysDictValueDTO);
        dictValueCache.put(cacheDictValueKey, sysDictValueDTO.tableKey(), sysDictValueDTO);
        return CommonDTO.ok();
    }

    /**
     * 修改字典值
     *
     * @param sysDictValueDTO
     * @return
     */
    @RequestMapping("/updateDictValue")
    public CommonDTO updateDictValue(@RequestBody SysDictValueDTO sysDictValueDTO) {
        int insert = valueDao.update(sysDictValueDTO);
        dictValueCache.put(cacheDictValueKey, sysDictValueDTO.tableKey(), sysDictValueDTO);
        return CommonDTO.ok();
    }

    /**
     * 删除字典值
     *
     * @param sysDictValueDTO
     * @return
     */
    @RequestMapping("/delDictValue")
    public CommonDTO delDictValue(@RequestBody SysDictValueDTO sysDictValueDTO) {
        int insert = valueDao.delete(sysDictValueDTO);
        dictValueCache.remove(cacheDictValueKey, sysDictValueDTO.tableKey());
        return CommonDTO.ok();
    }

    /**
     * 查询字典信息
     *
     * @return
     */
    @RequestMapping("/cacheDict")
    public CommonDTO loadCache() {
        CommonDTO commonDTO = CommonDTO.ok();
        List<SysDictKeyDTO> dictKeyList = new ArrayList<>();


        if (dictCache.hasKey(dictKey)) {
            dictKeyList = dictCache.getValues(dictKey);
        } else {
            dictKeyList = dictKeyDao.select();
            for (SysDictKeyDTO sysDictKeyDTO : dictKeyList) {
                dictCache.put(dictKey, sysDictKeyDTO.getDictKey(), sysDictKeyDTO);
            }
        }


        List<SysDictValueDTO> valueSelect = new ArrayList<>();

        if (dictValueCache.hasKey(cacheDictValueKey)) {
            valueSelect = dictValueCache.getValues(cacheDictValueKey);
        } else {
            valueSelect = valueDao.select();
            for (SysDictValueDTO sysDictValueDTO : valueSelect) {
                dictValueCache.put(cacheDictValueKey, sysDictValueDTO.tableKey(), sysDictValueDTO);
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("dictKey", dictKeyList);
        data.put("dictValue", valueSelect);
        commonDTO.setData(data);
        return commonDTO;
    }
}
