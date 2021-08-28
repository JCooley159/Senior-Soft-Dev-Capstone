package com.drim;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class HelloAspect {

    @Before("execution(* *(..)) && @annotation(HelloAnnotation)")
    public void before(JoinPoint joinPoint){
        System.out.println("Before Hello!");
    }

    @After("execution(* *(..)) && @annotation(HelloAnnotation)")
    public void after(JoinPoint joinPoint){
        System.out.println("After Hello!");
    }
}
