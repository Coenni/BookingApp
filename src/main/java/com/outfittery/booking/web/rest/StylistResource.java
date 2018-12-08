package com.outfittery.booking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.outfittery.booking.config.util.HeaderUtil;
import com.outfittery.booking.domain.Stylist;
import com.outfittery.booking.repository.StylistRepository;
import com.outfittery.booking.service.dto.StylistDto;
import com.outfittery.booking.web.rest.errors.BadRequestAlertException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import static com.outfittery.booking.web.rest.errors.ErrorConstants.ID_EXISTS;


/**
 * REST controller for managing Stylist.
 */
@RestController
@RequestMapping("/api")
public class StylistResource {

    private final Logger log = LoggerFactory.getLogger(StylistResource.class);

    private static final String ENTITY_NAME = "stylist";

    private final StylistRepository stylistRepository;

    public StylistResource(StylistRepository stylistRepository) {
        this.stylistRepository = stylistRepository;
    }


    /**
     * POST  /stylist : Create a new stylist.
     *
     * @param stylistDto the stylist to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stylist, or with status 400 (Bad Request) if the stylist has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stylist")
    @Timed
    public ResponseEntity<Stylist> createStylist(@Valid @RequestBody StylistDto stylistDto) throws URISyntaxException {
        log.info( "REST request to save Stylist : {}", stylistDto);
        if (stylistDto.getStylistId() != null) {
            throw new BadRequestAlertException("A new stylist cannot already have an ID", ENTITY_NAME, ID_EXISTS);
        }
        Stylist result = stylistRepository.save(this.convertToEntity(stylistDto));
        return ResponseEntity.created(new URI("/api/stylist/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stylist : get all the stylist.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of stylist data in body
     */
    @GetMapping("/stylist")
    @Timed
    public List<StylistDto> getAllStylist() {
        log.info( "REST request to get all Stylist");
        return stylistRepository.findAll().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    /**
     * GET  /stylist/:id : get the "id" stylist.
     *
     * @param id the id of the stylist to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stylist data, or with status 404 (Not Found)
     */
    @GetMapping("/stylist/{id}")
    @Timed
    public ResponseEntity<StylistDto> getStylist(@PathVariable Long id) {
        log.info( "REST request to get Stylist : {}", id);
        Optional<Stylist> stylist = stylistRepository.findById(id);

        return stylist.map((Stylist response) -> {
            return (ResponseEntity.ok()).body(this.convertToDto(response));
        }).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    private StylistDto convertToDto(Stylist stylist) {
        return new StylistDto(stylist.getId(), stylist.getName());
    }

    private Stylist convertToEntity(StylistDto stylistDto) {
        return new Stylist(stylistDto.getStylistId(), stylistDto.getStylistName());
    }
    
}
