package jcooley1.demo;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TimingAspect {

    @Pointcut("execution(* *(..))")
    public void allMethods() {}

	@Pointcut("execution(* jcooley1.demo..*(..))")
	public void methodsInCooleysPackage() {}


    @Around( "methodsInCooleysPackage() && @annotation(Timed)" )
    public Object profile( final ProceedingJoinPoint joinPoint) throws Throwable {
        final long start = System.currentTimeMillis();
        try {
            final Object value = joinPoint.proceed();
            return value;
        } catch (Throwable t) {
            throw t;
        } finally {
            final long stop = System.currentTimeMillis();
            System.out.println("Execution time of "+ joinPoint.getSignature().getName() + " : "+ (stop-start));
        }
    }
}
