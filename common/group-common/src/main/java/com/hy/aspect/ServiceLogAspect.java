package com.hy.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @Description: $- ServiceLogAspect -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/17 0:14
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/17 0:14
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Aspect
@Component
@Slf4j
public class ServiceLogAspect {
    /*
      AOP通知
      前置通知：在方法调用之前
      后置通知：在方法调用之后
      环绕通知：在方法调用之前和之后，都分别可以执行通知
      异常通知：如果方法调用过程中发生异常，则通知
      最终通知：在方法调用之后通知

     */


    /**
     * 切面表达式
     * execution 代表所要执行的表达主题
     * 第一处 * 代表方法返回类型 * 代表所有类型
     * 第二处 包名代表AOP 监控的类所在的包
     * 第三处 .. 代表该包及其子包下的所有类
     * 第四处 * 代表类名 *代表所有类
     * 第五处 *(..)  *代表类中的方法名  (..) 表示方法中的任何方法
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.hy..*.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("******************************** 开始执行 {}.{} ********************************",joinPoint.getTarget(), joinPoint.getSignature().getName());

        //记录开始时间
        long begin = System.currentTimeMillis();

        //执行目标service
        Object result = joinPoint.proceed();

        //记录结束时间
        long end = System.currentTimeMillis();


        //得到时间差
        long takeTime = end - begin;
        if (takeTime > 3000) {
            log.error("******************************** 执行结束，耗时： {} 毫秒 ********************************", takeTime);
        } else if (takeTime > 2000) {
            log.warn("******************************** 执行结束，耗时： {} 毫秒 ********************************", takeTime);
        } else {
            log.info("******************************** 执行结束，耗时： {} 毫秒 ********************************", takeTime);
        }

        return result;


    }

}
