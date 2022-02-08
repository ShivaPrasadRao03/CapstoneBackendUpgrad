package com.upgrad.bookmyconsultation.controller;

import com.upgrad.bookmyconsultation.entity.User;
import com.upgrad.bookmyconsultation.exception.InvalidInputException;
import com.upgrad.bookmyconsultation.service.AppointmentService;
import com.upgrad.bookmyconsultation.service.UserService;
import io.micrometer.core.instrument.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * Contains method to create an user,get user ,get appointments for an user
 *
 * @author Shiva Prasad
 */


@RestController
@RequestMapping("/users")
public class UserAdminController {


	@Autowired
	private UserService userService;

	@Autowired
	private AppointmentService appointmentService;


	@GetMapping(path = "/{id}")
	public ResponseEntity<User> getUser(@RequestHeader("authorization") String accessToken,
	                                    @PathVariable("id") final String userUuid) {
		final User User = userService.getUser(userUuid);
		return ResponseEntity.ok(User);
	}
	
	/*create a post method named createUser with return type as ResponseEntity
	define the method parameter user of type User. Set it final. Use @RequestBody for mapping.
	declare InvalidInputException using throws keyword
	register the user
	return http response with status set to OK*/


	//This method is used to register an user and returns an HTTP response
	@PostMapping("/register")
	public ResponseEntity<User> createUser(final @RequestBody User user ) throws InvalidInputException{
		System.out.println("Create User");
		userService.register(user);
		return ResponseEntity.ok(user);
	}


	//This method is used to get appointments for an User and returns an HTTP response
	@GetMapping("/{userId}/appointments")
	public ResponseEntity<Object> getAppointmentForUser(@PathVariable("userId") String userId) {
		return ResponseEntity.ok(appointmentService.getAppointmentsForUser(userId));
	}


}
