/**
 * <p>文件名称: DateUtil.java</p>
 * <p>文件描述: </p>
 * <p>版权所有: 版权所有(C)2016-</p>
 * <p>公    司: 金证财富南京科技有限公司</p>
 * <p>内容摘要: </p>
 * <p>其他说明: </p>
 * <p>创建日期： 2021/2/4 17:41 </p>
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
package com.example.billmanager.brickyard.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static DateFormat DATE_FORMATE = new SimpleDateFormat("yyyy-MM-dd");
    public static DateFormat YEAR_FORMATE = new SimpleDateFormat("yyyy");
    public static DateFormat MONTH_FORMATE_1 = new SimpleDateFormat("yyyy-MM");
    public static DateFormat MONTH_FORMATE = new SimpleDateFormat("MM");


    public static String getDateQuarter(Date date) {
        String year = YEAR_FORMATE.format(date);
        String month = MONTH_FORMATE.format(date);
        int i = 0;
        switch (Integer.parseInt(month)) {
            case 1:
            case 2:
            case 3:
                i = 1;
                break;
            case 4:
            case 5:
            case 6:
                i = 2;
                break;
            case 7:
            case 8:
            case 9:
                i = 3;
                break;
            case 10:
            case 11:
            case 12:
                i = 4;
                break;
        }
        return String.format("%s,%02d", year, i);
    }
}
