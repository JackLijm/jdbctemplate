/**
 * <p>文件名称: RestControllerAspect.java</p>
 * <p>文件描述: </p>
 * <p>版权所有: 版权所有(C)2016-</p>
 * <p>公    司: 金证财富南京科技有限公司</p>
 * <p>内容摘要: </p>
 * <p>其他说明: </p>
 * <p>创建日期： 2020/3/21 16:57 </p>
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
package com.example.jdbcteplate.common.aop;

import com.example.jdbcteplate.annotation.ExternalCall;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class RestControllerAspect {
    @Pointcut("execution(* com.example.jdbcteplate.*.controller.*.*(..))")
    public void endPoint() {
    }

    @Before("endPoint()")
    public void before(JoinPoint joinpoint) {
        System.out.println(String.format("请求入参:[%s]", Arrays.toString(joinpoint.getArgs())));
    }

    @AfterReturning(value = "endPoint()", returning = "result")
    public void after(JoinPoint joinpoint, Object result) {
        if (result != null) {
            System.out.println(String.format("返回结果:[%s]", result.toString()));
        }
    }

    /**
     * 切面+注解同时满足则进行切面
     *
     * @param joinpoint
     * @param result
     * @param externalCall
     */
    @AfterReturning(value = "endPoint()&&@annotation(externalCall)", returning = "result")
    public void afterWithAnno(JoinPoint joinpoint, Object result, ExternalCall externalCall) {
        System.out.println(String.format("加了注解的aop", result.toString()));
    }

    @AfterThrowing(value = "endPoint()", throwing = "e")
    public void after(JoinPoint joinpoint, Exception e) {
        System.out.println(String.format("返回结果:[%s]", e.toString()));
    }
}
