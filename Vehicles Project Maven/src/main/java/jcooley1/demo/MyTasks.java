package jcooley1.demo;

import java.util.ArrayList;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;



@Component
public class MyTasks
{
	private RestTemplate restTemplate = new RestTemplate();
	private int id = 1;


	@Log
	@Timed
	@Scheduled (fixedRate = 10000)   // every ten seconds
	public void addVehicle()
	{
		// URL that's mapped to the method we want
		String  postURL = "http://localhost:8080/addVehicle";

		// Create the Vehicle Object
		int year = RandomUtils.nextInt(1986,2017);
		double price = (double) RandomUtils.nextInt(15000,45001);
		String makeModel = RandomStringUtils.randomAlphabetic(5,10);
		Vehicle v = new Vehicle(id++, makeModel, year, price);

		// Execute on RestTemplate
		restTemplate.postForObject(postURL,v,Vehicle.class);
	}


	@Log
	@Timed
	@Scheduled (cron = "0 */2 * * * *") // every other minute
	public void deleteVehicle()
	{
		// Generate a random ID
		int deleteID = RandomUtils.nextInt(0,5);

		// Create the URL that takes in the ID as a parameter
		String deleteURL = "http://localhost:8080/deleteVehicle{" + deleteID + "}";
		String getURL = "http://localhost:8080/getVehicle{" + deleteID + "}";


		Vehicle v = restTemplate.getForObject(getURL, Vehicle.class);
		if (v != null)
		{
			// then execute the delete on RestTemplate
			restTemplate.delete(deleteURL);
		}

	}


	@Log
	@Timed
	@Scheduled (cron = "0 * * * * *") // once every minute
	public void updateVehicle()
	{
		// URL that's mapped to the method we want
		String  putURL = "http://localhost:8080/updateVehicle";

		// Create the new Vehicle Object to replace the old one with
		int newID = RandomUtils.nextInt(1,5);
		int year = 4444;
		double price = (double) 44444;
		String makeModel = "Four-Runner";
		Vehicle v = new Vehicle(newID, makeModel, year, price);


		// Execute on RestTemplate
		restTemplate.put(putURL, v);

	}


	@Log
	@Timed
	@Scheduled (cron = "0 0 * * * *") // at minute 0 of every hour
	public void getLatestVehicles()
	{
		ArrayList<Vehicle> list = new ArrayList<>();

		System.out.println("Fetching latest vehicles:");

		// URL that's mapped to the method we want
		String getURL = "http://localhost:8080/getLatestVehicles";

		// execute on rest template and save to the arraylist
		list = restTemplate.getForObject(getURL, ArrayList.class);


		for (Vehicle v : list)
		{
			// print them to the console
			System.out.println(v.toString());
		}
	}


}
