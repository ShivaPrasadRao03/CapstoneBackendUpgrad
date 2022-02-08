package com.upgrad.bookmyconsultation.controller;

import com.upgrad.bookmyconsultation.entity.Rating;
import com.upgrad.bookmyconsultation.service.RatingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contains method to submit ratings
 *
 * @author Shiva Prasad
 */


@RestController
public class RatingsController {

	@Autowired
	private RatingsService ratingsService;


	/* Comments provided by UPGRAD
	create a post method named submitRatings with return type as ResponseEntity
	define the method parameter rating of type Rating, use @RequestBody for mapping
	submit the ratings
	return http response with status set to OK*/


	//This method is used to Submit ratings provided by an User for an doctor and returns Http Response
	@PostMapping("/ratings")
	public ResponseEntity<Object> submitRatings(@RequestBody Rating rating){
		ratingsService.submitRatings(rating);
		return new ResponseEntity<>("Success",HttpStatus.OK);
	}
	
}
