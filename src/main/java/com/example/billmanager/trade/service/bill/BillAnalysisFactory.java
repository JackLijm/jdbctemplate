package com.example.billmanager.trade.service.bill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BillAnalysisFactory {

    @Autowired
    ApplicationContext applicationContext;

    public BillAnalysisService getInstance(String billType){
        switch (billType){
            case "1":
                return (BillAnalysisService) applicationContext.getBean("aliBillAnalysis");
            case "2":
                return (BillAnalysisService) applicationContext.getBean("weChartBillAnalysis");
            case "3":
                return (BillAnalysisService) applicationContext.getBean("cmbBillAnalysis");
            case "4":
                return (BillAnalysisService) applicationContext.getBean("pabcBillAnalysis");
        }
        return null;
    }
}
