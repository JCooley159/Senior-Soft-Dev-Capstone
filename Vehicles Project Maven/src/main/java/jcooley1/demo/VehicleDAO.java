package jcooley1.demo;

import java.math.BigInteger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



/**
 * Data Access Object - provide some specific data operations without exposing details of the database
 * Access data for the Vehicle entity.
 * Repository annotation allows Spring to find and configure the DAO.
 * Transactional annonation will cause Spring to call begin() and commit()
 * at the start/end of the method. If exception occurs it will also call rollback().
 */
@Repository
@Transactional
public class VehicleDAO
{
	// PersistenceContext annotation used to specify there is a database source.
	// EntityManager is used to create and remove persistent entity instances,
	// to find entities by their primary key, and to query over entities.

	@PersistenceContext
	private EntityManager entityManager;



	// CREATE  aka  PERSIST  -  insert vehicle into the db
	public void create(Vehicle v)	{
		entityManager.persist(v);
	}



	// READ  aka  FIND  -  return the vehicle that matches the id
	public Vehicle getByID(int id)	{
		return entityManager.find(Vehicle.class, id);
	}



	// UPDATE  aka  MERGE  -  update the vehicle in the db
	public void update(Vehicle v) {
		entityManager.merge(v);
	}



	// DELETE  aka  REMOVE  -  remove the vehicle from the db
	public void delete(int id) {
		Vehicle v = entityManager.find(Vehicle.class,id);
		if (v != null)	{
			entityManager.remove(v);
		}
	}



	public int getCount() {
		// create the query ON the entity manager
		Query q = entityManager.createNativeQuery("select count(*) from vehicles");
		// then save and return the resulting number
		BigInteger count = (BigInteger) q.getSingleResult();
		return count.intValue();
	}

	// This doesn't work, but hey, maybe I tried!
	//
	// public int getNumOfFourRunners() {
	// 	// create the query ON the entity manager
	// 	Query q =
	// 		entityManager.createNativeQuery("select count(*) from vehicles where makeModel = :makeModel").setParameter("Four-Runner",
	// 			makeModel).getSingleResult();
	// 	// then save and return the resulting number
	// 	BigInteger count = (BigInteger) q.getSingleResult();
	// 	return count.intValue();
	// }

}