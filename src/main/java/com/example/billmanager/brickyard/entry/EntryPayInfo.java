/**
 * <p>文件名称: EntryPayInfo.java</p>
 * <p>文件描述: </p>
 * <p>版权所有: 版权所有(C)2016-</p>
 * <p>公    司: 金证财富南京科技有限公司</p>
 * <p>内容摘要: </p>
 * <p>其他说明: </p>
 * <p>创建日期： 2021/2/6 13:12 </p>
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
package com.example.billmanager.brickyard.entry;

import com.example.billmanager.brickyard.dto.PayInfoDto;
import com.example.billmanager.common.CommonState;
import com.example.billmanager.dao.PayInfoDao;

import java.util.ArrayList;
import java.util.List;

public class EntryPayInfo {

    private List<PayInfoDto> payInfoList;

    public EntryPayInfo() {
        payInfoList = new ArrayList<>();
    }

    public void addPayInfo(PayInfoDto nPayInfoDto) {
        payInfoList.add(nPayInfoDto);
    }

    public int insertDb(PayInfoDao nPayInfoDao) {
        int i = 0;
        for (PayInfoDto payInfoDto : payInfoList) {
            if (CommonState.OPERATOR_TYPE_CREATE.equals(payInfoDto.getOperateType())) {
                i += nPayInfoDao.insertPayInfo(payInfoDto);
            }
        }
        return i;
    }
}
