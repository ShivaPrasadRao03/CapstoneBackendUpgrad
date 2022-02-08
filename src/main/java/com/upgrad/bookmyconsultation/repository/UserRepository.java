package com.upgrad.bookmyconsultation.repository;

import com.upgrad.bookmyconsultation.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * UserRepository For CRUD Operations
 * This interface Contains method declaration to retrieve all users and also by its email ID
 * @author Shiva Prasad
 */

@Repository
public interface UserRepository extends CrudRepository<User, String> {

	@Override
	List<User> findAll();

	//specify a method that returns User by finding it by email id
	User findByEmailId(String emailId);
}
