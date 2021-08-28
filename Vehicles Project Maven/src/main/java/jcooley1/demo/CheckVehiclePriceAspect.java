package jcooley1.demo;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;


@Component
@Aspect
public class CheckVehiclePriceAspect
{

	// this pointcut courtesy of Dr. Im
	@Pointcut("execution(* jcooley1.demo..*(Vehicle))")
	public void publicMethodAcceptingVehicle() {}


	// this advice courtesy of Dr. Im
	@Around("publicMethodAcceptingVehicle() && @annotation(CheckVehiclePrice)")
	public void checkVehiclePrice( final ProceedingJoinPoint pjp ) throws Throwable {

		Vehicle v = (Vehicle) pjp.getArgs()[0];
		System.out.println("### Checking Vehicle: " + v.toString());

		if (v.getRetailPrice() > 10000)
		{
			// only accept (ADD/UPDATE) when vehicle price is acceptable
			pjp.proceed();
		} else {
			// do nothing for unacceptable vehicles
			System.out.println("Rejecting vehicle... Do nothing");
		}
	}

}
