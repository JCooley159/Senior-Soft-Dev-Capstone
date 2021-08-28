package com.example.hellorestdatabase2;

import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;


/**
 * Data Access Object - provide some specific data operations without exposing details of the database
 * Access data for the Greeting entity.
 * Repository annotation allows Spring to find and configure the DAO.
 * Transactional annotation will cause Spring to call begin() and commit()
 * at the start/end of the method. If exception occurs it will also call rollback().
 */

@Repository
@Transactional
public class GreetingDAO
{


	//PersistenceContext annotation used to specify there is a database source.
	// EntityManager is used to create and remove persistent entity instances,
	// to find entities by their primary key, and to query over entities.
	@PersistenceContext
	private EntityManager entityManager;

	// Insert greeting into the database.
	public void create(Greeting greeting)
	{
		entityManager.persist(greeting);
		return;
	}
	// Return the greeting with the passed-in id.

	public Greeting getById(int id)
	{
		return entityManager.find(Greeting.class, id);
	}


}
