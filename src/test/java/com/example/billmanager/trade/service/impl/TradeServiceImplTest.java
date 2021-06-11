package com.example.billmanager.trade.service.impl;

import com.example.billmanager.trade.service.TradeService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


@RunWith(SpringRunner.class)
@SpringBootTest
class TradeServiceImplTest {
    @Autowired
    TradeService tradeService;
    @Resource
    RedisTemplate redisTemplate;

    @Test
    void tradeTypeReport() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("test-lijm", "4");

        Object o = valueOperations.get("test-lijm");
        System.out.println(o);
    }
}



