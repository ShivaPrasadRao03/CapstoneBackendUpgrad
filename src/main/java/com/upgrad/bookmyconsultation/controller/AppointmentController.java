package com.upgrad.bookmyconsultation.controller;

import com.upgrad.bookmyconsultation.entity.Appointment;
import com.upgrad.bookmyconsultation.exception.InvalidInputException;
import com.upgrad.bookmyconsultation.exception.SlotUnavailableException;
import com.upgrad.bookmyconsultation.service.AppointmentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Contains method to book an appointment,get appointment
 *
 * @author Shiva Prasad
 */

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private static final Logger logger = LogManager.getLogger(AppointmentController.class);


    @Autowired
    private AppointmentService appointmentService;


	/* Comments provided by UPGRAD
	create a method post method named bookAppointment with return type ReponseEntity
	method has parameter of type Appointment, use RequestBody Annotation for mapping
	save the appointment details to the database and save the response from the method used
	return http response using ResponseEntity */


    //This method is used to book an appointment and returns an HTTP Response
    @PostMapping("/bookAppointment")
    public ResponseEntity<Object> bookAppointment(@RequestBody Appointment appointment) throws  InvalidInputException{

        logger.info("**************Book appointment*********");
        logger.info("Date="+appointment.getAppointmentDate());
        logger.info("Time="+appointment.getTimeSlot());
        logger.info("Created Date="+appointment.getCreatedDate());
        String appointmentID = appointmentService.appointment(appointment);

        return new ResponseEntity<>(appointmentID, HttpStatus.OK);
    }

	/* Comments provided by UPGRAD
	create a get method named getAppointment with return type as ResponseEntity
    method has appointmentId of type String. Use PathVariable annotation to identity appointment using the parameter defined
	get the appointment details using the appointmentId
    save the response
    return the response as an http response */


    //This method is used to fetch an appointment by its ID and returns an HTTP Response
    @GetMapping("/{appointmentId}")
    public ResponseEntity<Object> getAppointment(@PathVariable("appointmentId") String appointmentId) {
        Appointment appointment = appointmentService.getAppointment(appointmentId);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

}