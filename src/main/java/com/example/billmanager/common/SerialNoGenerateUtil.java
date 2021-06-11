package com.example.billmanager.common;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Component
public class SerialNoGenerateUtil {
    private static DateFormat TIME_FORMATE =  new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public String getSerialNo(){
        return TIME_FORMATE.format(new Date()) + new Random().nextInt(100);
    }
}
