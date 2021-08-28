package jcooley1.demo;

import javax.persistence.*;
import org.jetbrains.annotations.NotNull;


/**
 * The Entity annotation indicates that this class is a JPA entity.
 * The Table annotation specifies the name for the table in the db.
 */
@Entity
@Table (name = "vehicles")
public class Vehicle implements Comparable<Vehicle>
{
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	private long id;

	private String makeModel;
	private int year;
	private double retailPrice;


	public Vehicle() {
		this.id = 0;
		this.makeModel = "";
		this.year = 0;
		this.retailPrice = 0.0;
	}


	public Vehicle(long id, String makeModel, int year, double retailPrice) {
		this.id = id;
		this.makeModel = makeModel;
		this.year = year;
		this.retailPrice = retailPrice;
	}


	public long getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}


	public String getMakeModel()
	{
		return makeModel;
	}
	public void setMakeModel(String makeModel)
	{
		this.makeModel = makeModel;
	}


	public int getYear()
	{
		return year;
	}
	public void setYear(int year)
	{
		this.year = year;
	}


	public double getRetailPrice()
	{
		return retailPrice;
	}
	public void setRetailPrice(double retailPrice)
	{
		this.retailPrice = retailPrice;
	}


	@Override
	public int compareTo(@NotNull Vehicle o) {
		return (int) (this.id - o.getId());
	}

}
