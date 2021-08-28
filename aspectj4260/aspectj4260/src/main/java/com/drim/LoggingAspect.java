package com.drim;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {

    @Pointcut("execution(public * *(..))")
    public void publicMethod() {}

    @Pointcut("execution(* *..*(..))")
    public void methodsInExamplesPackage() {}

    //@Before("publicMethod() && methodsInExamplesPackage()")

    @Before("publicMethod()")
    public void addLog( final JoinPoint joinPoint ) {
        System.out.println("Executing: "+joinPoint.getSignature());

        Object[] arguments = joinPoint.getArgs();
        for (Object argument : arguments) {
            if (argument != null) {
                System.out.println(argument.getClass().getSimpleName() + " = " + argument);
            }
        }
    }

}
