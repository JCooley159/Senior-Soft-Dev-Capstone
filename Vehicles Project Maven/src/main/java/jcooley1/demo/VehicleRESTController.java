package jcooley1.demo;

import java.io.IOException;
import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class VehicleRESTController
{

	private VehicleDAO vehicleDAO;


	@Log
	@Timed
	@RequestMapping(value="/addVehicle", method=RequestMethod.POST)
	public Vehicle addVehicle(@RequestBody Vehicle newVehicle) throws IOException
	{
		vehicleDAO.create(newVehicle);
		return newVehicle;
	}


	@Log
	@Timed
	@RequestMapping(value="/getVehicle/{id}", method=RequestMethod.GET)
	public Vehicle getVehicle(@PathVariable("id") int id) throws IOException
	{
		return vehicleDAO.getByID(id);
	}


	@Log
	@Timed
	@RequestMapping(value = "/getNumberOfVehicles", method = RequestMethod.GET)
	public int getNumberOfVehicles() throws IOException
	{
		// gets the size of the inventory
		return vehicleDAO.getCount();
	}



	@Log
	@Timed
	@RequestMapping(value = "/updateVehicle", method = RequestMethod.PUT)
	public Vehicle updateVehicle(@RequestBody Vehicle newVehicle) throws IOException
	{
		vehicleDAO.update(newVehicle);
		return newVehicle;
	}


	@Log
	@Timed
	@CheckBeforeDelete
	@RequestMapping(value = "/deleteVehicle/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteVehicle(@PathVariable("id") int id) throws IOException
	{
		// saves time by exiting early if id is invalid
		if(id==0)
		{
			return new ResponseEntity<String>("Sorry vehicle ID does not exist", HttpStatus.NOT_FOUND);
		}


		// tries to find the vehicle
		Vehicle v = null;
		v = vehicleDAO.getByID(id);



		if (v != null)
		{
			vehicleDAO.delete(id);
			return new ResponseEntity<String>("Vehicle deleted", HttpStatus.FOUND);
		}
		else
		{
			return new ResponseEntity<String>("Sorry vehicle ID not found", HttpStatus.NOT_FOUND);
		}
	}


	@Log
	@Timed
	@RequestMapping(value = "/getLatestVehicles", method = RequestMethod.GET)
	public ArrayList<Vehicle> getLatestVehicles() throws IOException
	{
		// creates a new arraylist
		ArrayList<Vehicle> list = new ArrayList<>();

		// gets the size of the inventory
		int count = vehicleDAO.getCount();

		if (count >= 10)
		{
			// gets each of the last ten vehicles in the inventory
			for(int i = count - 9; i <= count; i++)
			{
				// and adds them to our arraylist
				list.add( vehicleDAO.getByID(i) );
			}
		}
		else
		{
			System.out.println("Sorry, there aren't enough vehicles yet!");
		}

		return list;
	}


}
