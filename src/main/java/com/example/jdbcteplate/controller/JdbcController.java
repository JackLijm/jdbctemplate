/**
 * <p>文件名称: JdbcController.java</p>
 * <p>文件描述: </p>
 * <p>版权所有: 版权所有(C)2016-</p>
 * <p>公    司: 金证财富南京科技有限公司</p>
 * <p>内容摘要: </p>
 * <p>其他说明: </p>
 * <p>创建日期： 2020/3/10 12:20 </p>
 * <p>完成日期：</p>
 * <p>修改记录1:</p>
 * <pre>
 *    修改日期：
 *    版 本 号：
 *    修 改 人：
 *    修改内容：
 * </pre>
 * <p>修改记录2：…</p>
 *
 * @version 1.0
 * @author lijm@szkingdom.com
 */
package com.example.jdbcteplate.controller;

import com.example.jdbcteplate.annotation.ExternalCall;
import com.example.jdbcteplate.dao.OracleJdbcDao;
import com.example.jdbcteplate.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class JdbcController {
    @Autowired
    OracleJdbcDao dao;

    @RequestMapping("/querySysDate")
    @ExternalCall(name = "hello-world")
    public String test(String xxx) {
        Map map = new HashMap<>();
        map.put("xxx", xxx);
        List<Map> maps = dao.qryAcctBlack(map);
        int a = 1 / 0;
        return dao.querySystemDate(map);
    }

    @RequestMapping("/testException")
    public String testException(String xxx) throws BusinessException {
        throw new BusinessException("111", "223");
    }
}
