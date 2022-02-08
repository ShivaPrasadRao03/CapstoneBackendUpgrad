package com.upgrad.bookmyconsultation.service;

import com.upgrad.bookmyconsultation.controller.AppointmentController;
import com.upgrad.bookmyconsultation.entity.Appointment;
import com.upgrad.bookmyconsultation.exception.InvalidInputException;
import com.upgrad.bookmyconsultation.exception.ResourceUnAvailableException;
import com.upgrad.bookmyconsultation.exception.SlotUnavailableException;
import com.upgrad.bookmyconsultation.repository.AppointmentRepository;
import com.upgrad.bookmyconsultation.repository.UserRepository;
import com.upgrad.bookmyconsultation.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Contains method to save an appointment,get appointment by appointmentID ,get appointments by userID
 *
 * @author Shiva Prasad
 */

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private static final Logger logger = LogManager.getLogger(AppointmentService.class);

	
	/* Comments provided by UPGRAD
	mark it autowired
	create an instance of AppointmentRepository called appointmentRepository*/


    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;


	/* Comments provided by UPGRAD
	create a method name appointment with the return type of String and parameter of type Appointment
	declare exceptions 'SlotUnavailableException' and 'InvalidInputException'
		validate the appointment details using the validate method from ValidationUtils class
		find if an appointment exists with the same doctor for the same date and time
		if the appointment exists throw the SlotUnavailableException
		save the appointment details to the database
		return the appointment id*/


    //This method is used to save the appointment objects into DB and also and also throws required exceptions wherever necessary
    public String appointment(Appointment appointment) throws SlotUnavailableException, InvalidInputException {
        ValidationUtils.validate(appointment);
        if (appointmentRepository.findByDoctorIdAndTimeSlotAndAppointmentDate(appointment.getDoctorId(), appointment.getTimeSlot(), appointment.getAppointmentDate()) != null) {
            throw new SlotUnavailableException("Slot Not Available");
        }
        appointmentRepository.save(appointment);
        return appointment.getAppointmentId();
    }


    /* Comments provided by UPGRAD
    create a method getAppointment of type Appointment with a parameter name appointmentId of type String
    Use the appointmentid to get the appointment details
    if the appointment exists return the appointment
    else throw ResourceUnAvailableException
    tip: use Optional.ofNullable(). Use orElseThrow() method when Optional.ofNullable() throws NULL*/


    //This method is used to fetch appointments provided by its ID also throws required exceptions wherever necessary
    public Appointment getAppointment(String appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        logger.info("Appointment=" + appointment);
        if (appointment.isPresent()) {
            return appointment.get();
        }
        throw new ResourceUnAvailableException();
    }


    public List<Appointment> getAppointmentsForUser(String userId) {
        return appointmentRepository.findByUserId(userId);
    }
}
