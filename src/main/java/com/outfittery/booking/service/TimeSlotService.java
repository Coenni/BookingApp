package com.outfittery.booking.service;

import com.outfittery.booking.domain.TimeSlot;
import com.outfittery.booking.domain.embeddedids.TimeSlotPK;
import com.outfittery.booking.repository.StylistRepository;
import com.outfittery.booking.repository.TimeSlotRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing TimeSlot.
 */
@Service
@Transactional
public class TimeSlotService {

    private final Logger log = LoggerFactory.getLogger(TimeSlotService.class);

    private final TimeSlotRepository timeSlotRepository;
    private final StylistRepository stylistRepository;

    public TimeSlotService(TimeSlotRepository timeSlotRepository, StylistRepository stylistRepository) {
        this.timeSlotRepository = timeSlotRepository;
        this.stylistRepository = stylistRepository;
    }

    /**
     * Save a stationShift.
     *
     * @param timeSlot the entity to save
     * @return the persisted entity
     */
    public TimeSlot save(TimeSlot timeSlot) {
        log.debug("Request to save timeSlot : {}", timeSlot);        
        return timeSlotRepository.save(timeSlot);
    }

    
    /**
     * Get one timeSlot by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<TimeSlot> findOne(TimeSlotPK id) {
        log.debug("Request to get timeSlot : {}", id);
        return timeSlotRepository.findById(id);
    }

    /**
     *
     * @return list of all TimeSlot
     */
    @Transactional(readOnly = true)
    public List<TimeSlot> findAll() {
        log.debug("Request to get all timeSlot : {}");
        return timeSlotRepository.findAll();
    }

    /**
     * Get one timeSlot by id.
     *
     * @param timeSlots the list of timeslot to save
     * @return the stored list
     */
    @Transactional
    public List<TimeSlot> saveAll(List<TimeSlot> timeSlots) {
        return timeSlotRepository.saveAll(timeSlots);
    }

    @Transactional(readOnly = true)
    public List<TimeSlot> findAvailableSlots() {
        Long numberOfStylists = stylistRepository.count();
        return timeSlotRepository.findByAppointmentCounterLessThan(numberOfStylists);
    }

    public Boolean checkIfAlreadyExist(List<TimeSlotPK> timeSlotPKS) {
        return timeSlotRepository.findByIdIn(timeSlotPKS).size() > 0;
    }
}
