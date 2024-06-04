package com.example.myweb.aop;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/*
 * ex04: AOP 공통적인 기능(예: 로깅, 트랜잭션 관리)이 코드를 재 사용
 */
@Aspect
@Log4j2
@Component
public class LogAdvice {
    // 각 메서드 실행 전에 로깅
    @Before( "execution(* com.example.myweb.service.SampleService*.*(..))")
    public void logBefore() {
        log.info("========================");
    }
    // 두 개의 문자열 매개변수를 로깅
    @Before("execution(* com.example.myweb.service.SampleService*.doAdd(String, String)) && args(str1, str2)")
    public void logBeforeWithParam(String str1, String str2) {
        log.info("str1: " + str1);
        log.info("str2: " + str2);
    }
    // 클래스의 어떤 메서드에서 예외가 발생할 때 예외 메시지와 예외 객체를 로깅
    @AfterThrowing(pointcut = "execution(* com.example.myweb.service.SampleService*.*(..))", throwing="exception")
    public void logException(Exception exception) {

        log.info("Exception....!!!!");
        log.info("exception: "+ exception);
    }
    // 대상 객체, 메서드 매개변수, 실행 시간, 메서드 결과를 로깅
    @Around("execution(* com.example.myweb.service.SampleService*.*(..))")
    public Object logTime( ProceedingJoinPoint pjp) {

        long start = System.currentTimeMillis();

        log.info("Target: " + pjp.getTarget());
        log.info("Param: " + Arrays.toString(pjp.getArgs()));

        //invoke method
        Object result = null;
        try {
            result = pjp.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        log.info("TIME: "  + (end - start));

        return result;
    }
}
