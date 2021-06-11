/**
 * <p>文件名称: DtoUtils.java</p>
 * <p>文件描述: </p>
 * <p>版权所有: 版权所有(C)2016-</p>
 * <p>公    司: 金证财富南京科技有限公司</p>
 * <p>内容摘要: </p>
 * <p>其他说明: </p>
 * <p>创建日期： 2021/2/2 14:05 </p>
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
package com.example.billmanager.dto;

    import com.example.billmanager.common.CommonState;

    import java.util.Date;

public class DtoUtils {

    public static void setCommonInfo(BaseDTO baseDTO, String operator) {
        baseDTO.setCreatedDate(new Date());
        baseDTO.setUpdatedDate(new Date());
        baseDTO.setCreatedBy(operator);
        baseDTO.setUpdatedBy(operator);
        baseDTO.setOperateType(CommonState.OPERATOR_TYPE_CREATE);
        baseDTO.setDataState("2");
    }
}
