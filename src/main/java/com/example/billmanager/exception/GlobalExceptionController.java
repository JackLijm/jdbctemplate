/**
 * <p>文件名称: GlobalExceptionController.java</p>
 * <p>文件描述: </p>
 * <p>版权所有: 版权所有(C)2016-</p>
 * <p>公    司: 金证财富南京科技有限公司</p>
 * <p>内容摘要: </p>
 * <p>其他说明: </p>
 * <p>创建日期： 2020/3/21 15:39 </p>
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
package com.example.billmanager.exception;

import com.example.billmanager.dto.CommonDTO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * RestController全局异常处理类
 */
@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(Exception.class)
    public CommonDTO exception(Exception e){
        e.printStackTrace();
        return CommonDTO.err(e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public CommonDTO businessException(BusinessException e){
        e.printStackTrace();
        return CommonDTO.err(e.getError());
    }
}
