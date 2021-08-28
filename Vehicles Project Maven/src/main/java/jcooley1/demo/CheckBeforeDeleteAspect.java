package jcooley1.demo;

import java.util.Scanner;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;


@Component
@Aspect
public class CheckBeforeDeleteAspect
{
	// should execute on the deleteVehicle method in the RESTController
	@Pointcut("execution(* jcooley1.demo..*.deleteVehicle(..))")
	public void deleteMethods() {}


	// I can't figure out how to work around the error it's giving me here:
	// Error:(19, 0) ajc: applying to join point that doesn't return void:
	// method-execution(org.springframework.http.ResponseEntity
	//          jcooley1.demo.VehicleRESTController.deleteVehicle(int))
	@Around ("deleteMethods() && @annotation(CheckBeforeDelete)")
	public void checkDelete( final ProceedingJoinPoint pjp ) throws Throwable {

		System.out.println("Are you sure you want to delete this vehicle? (Y/N)");
		Scanner scanner = new Scanner(System.in);
		String answer = scanner.nextLine();

		if (answer.equalsIgnoreCase("Y"))
		{
			// only proceed to delete when the user says it's ok
			System.out.println("Okay, proceeding to delete:");
			pjp.proceed();
		} else {
			System.out.println("Delete cancelled.");
		}
	}

}
