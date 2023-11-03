package edu.ucmo.cbbackend.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimeTrace {

    // limit to controller
    @Around("execution(* edu.ucmo.cbbackend.controller..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        System.out.println("Method: " + joinPoint.toLongString());

        try {
            return joinPoint.proceed();
        } finally {
            long endtime = System.currentTimeMillis();
            long exetime = endtime - start;
            System.out.println("execution time = " + joinPoint.toString() + " " + exetime + " ms");
        }

    }


}
