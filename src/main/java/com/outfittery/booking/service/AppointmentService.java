package com.outfittery.booking.service;

import com.outfittery.booking.domain.Appointment;
import com.outfittery.booking.domain.Stylist;
import com.outfittery.booking.domain.TimeSlot;
import com.outfittery.booking.repository.AppointmentRepository;
import com.outfittery.booking.repository.TimeSlotRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Appointment.
 */
@Service
@Transactional
public class AppointmentService {

    private final Logger log = LoggerFactory.getLogger(AppointmentService.class);

    private final AppointmentRepository appointmentRepository;

    private final TimeSlotRepository timeSlotRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, TimeSlotRepository timeSlotRepository) {
        this.appointmentRepository = appointmentRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    /**
     * Save a stationShift.
     *
     * @param appointment the entity to save
     * @return the persisted entity
     */
    public Appointment save(Appointment appointment) {
        log.debug("Request to save appointment : {}", appointment);
        TimeSlot timeSlot = appointment.getTimeSlot();
        timeSlot.setAppointmentCounter(timeSlot.getAppointmentCounter()+1);
        timeSlotRepository.save(timeSlot);
        return appointmentRepository.save(appointment);
    }

    
    /**
     * Get one appointment by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Appointment> findOne(Long id) {
        log.debug("Request to get appointment : {}", id);
        return appointmentRepository.findById(id);
    }


    public List<Appointment> findAll() {
        log.debug("Request to get all appointments ");
        return appointmentRepository.findAll();
    }

    public Stylist findAvailableStylist(TimeSlot timeSlot) {
        List<Stylist> stylistsAvailable = appointmentRepository.findAvailableStylist(timeSlot);
        if(stylistsAvailable.size() > 0 ){
            return stylistsAvailable.get(0);
        }
        return null;
    }
}
