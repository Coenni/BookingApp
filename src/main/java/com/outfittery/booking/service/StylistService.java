package com.outfittery.booking.service;

import com.outfittery.booking.domain.Stylist;
import com.outfittery.booking.repository.StylistRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Stylist.
 */
@Service
@Transactional
public class StylistService {

    private final Logger log = LoggerFactory.getLogger(StylistService.class);

    private final StylistRepository stylistRepository;

    public StylistService(StylistRepository stylistRepository) {
        this.stylistRepository = stylistRepository;
    }

    /**
     * Save a stationShift.
     *
     * @param stylist the entity to save
     * @return the persisted entity
     */
    public Stylist save(Stylist stylist) {
        log.debug("Request to save stylist : {}", stylist);        
        return stylistRepository.save(stylist);
    }

    
    /**
     * Get one stylist by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Stylist> findOne(Long id) {
        log.debug("Request to get stylist : {}", id);
        return stylistRepository.findById(id);
    }

    
}
