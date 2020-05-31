package com.example.jdbcteplate.trade.service.impl;

import com.example.jdbcteplate.dto.EChartsDTO;
import com.example.jdbcteplate.trade.service.TradeService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
class TradeServiceImplTest {
    @Autowired
    TradeService tradeService;

    @Test
    void tradeTypeReport() {
        EChartsDTO eChartsDTO = tradeService.tradeTypeReport();
        System.out.println(eChartsDTO);
    }
}