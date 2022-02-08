package com.upgrad.bookmyconsultation.service;

import com.upgrad.bookmyconsultation.entity.Address;
import com.upgrad.bookmyconsultation.entity.Doctor;
import com.upgrad.bookmyconsultation.enums.Speciality;
import com.upgrad.bookmyconsultation.exception.InvalidInputException;
import com.upgrad.bookmyconsultation.exception.ResourceUnAvailableException;
import com.upgrad.bookmyconsultation.model.TimeSlot;
import com.upgrad.bookmyconsultation.repository.AddressRepository;
import com.upgrad.bookmyconsultation.repository.AppointmentRepository;
import com.upgrad.bookmyconsultation.repository.DoctorRepository;
import com.upgrad.bookmyconsultation.util.ValidationUtils;
import lombok.extern.log4j.Log4j2;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.Cacheable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Contains method to save an doctor and his address,fetch docter by doctorID ,
 * fetch all doctors by speciality,fetch doctors by ratings,fetch timeslots by doctorID and date
 *
 * @author Shiva Prasad
 */

@Log4j2
@Service
public class DoctorService {

    private static final Logger logger = LogManager.getLogger(DoctorService.class);

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private AddressRepository addressRepository;

	
	/* Comments provided by UPGRAD
	create a method register with return type and parameter of typeDoctor
	declare InvalidInputException for the method
		validate the doctor details
		if address is null throw InvalidInputException
		set UUID for doctor using UUID.randomUUID.
		if speciality is null
			set speciality to Speciality.GENERAL_PHYSICIAN
		Create an Address object, initialise it with address details from the doctor object
		Save the address object to the database. Store the response.
		Set the address in the doctor object with the response
		save the doctor object to the database
		return the doctor object*/

	//This method is used to save/create the doctor objects and throws required exception wherever necessary
    public Doctor register(Doctor doctor) throws InvalidInputException {
        ValidationUtils.validate(doctor);
        logger.debug("Doctor ID=" + doctor.getId());
        if (doctor.getAddress() == null)
            throw new InvalidInputException(Arrays.asList("Address"));
        doctor.setId(UUID.randomUUID().toString());
        if (doctor.getSpeciality() == null) {
            doctor.setSpeciality(Speciality.GENERAL_PHYSICIAN);
        }
        Address address = doctor.getAddress();
        address.setId(doctor.getId());
        logger.debug("Address ID ="+address.getId());
        doctor.setAddress(addressRepository.save(address));
        doctorRepository.save(doctor);
        return doctor;

    }
	
	
	/* Comments provided by UPGRAD
	create a method name getDoctor that returns object of type Doctor and has a String parameter called id
		find the doctor by id
		if doctor is found return the doctor
		else throw ResourceUnAvailableException*/

	//This method is used to retrieve doctor by its ID
    public Doctor getDoctor(String id) {
        Doctor doctor = doctorRepository.findById(id).get();
        if (doctor == null) {
            throw new ResourceUnAvailableException();
        }
        return doctor;
    }

    public List<Doctor> getAllDoctorsWithFilters(String speciality) {
		if (speciality != null && !speciality.isEmpty()) {
            return doctorRepository.findBySpecialityOrderByRatingDesc(Speciality.valueOf(speciality));
        }
        return getActiveDoctorsSortedByRating();
    }

    @Cacheable(value = "doctorListByRating")
    private List<Doctor> getActiveDoctorsSortedByRating() {
//		log.info("Fetching doctor list from the database");
        return doctorRepository.findAllByOrderByRatingDesc()
                .stream()
                .limit(20)
                .collect(Collectors.toList());
    }

    public TimeSlot getTimeSlots(String doctorId, String date) {

        TimeSlot timeSlot = new TimeSlot(doctorId, date);
        timeSlot.setTimeSlot(timeSlot.getTimeSlot()
                .stream()
                .filter(slot -> {
                    return appointmentRepository
                            .findByDoctorIdAndTimeSlotAndAppointmentDate(timeSlot.getDoctorId(), slot, timeSlot.getAvailableDate()) == null;

                })
                .collect(Collectors.toList()));

        return timeSlot;

    }
}
