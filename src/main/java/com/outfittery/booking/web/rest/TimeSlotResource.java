package com.outfittery.booking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.outfittery.booking.config.RuleValidator;
import com.outfittery.booking.config.SlotConfiguration;
import com.outfittery.booking.config.util.Constants;
import com.outfittery.booking.domain.TimeSlot;
import com.outfittery.booking.domain.embeddedids.TimeSlotPK;
import com.outfittery.booking.service.TimeSlotService;
import com.outfittery.booking.service.dto.TimeSlotDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;


/**
 * REST controller for managing TimeSlot.
 */
@RestController
@RequestMapping("/api")
public class TimeSlotResource {

    private static final String ENTITY_NAME = "timeslot";
    private final Logger log = LoggerFactory.getLogger(TimeSlotResource.class);

    private Integer INITIAL_APPOINTMENT_COUNT = 0;
    private Integer INITIAL_VERSION = 1;
    private final TimeSlotService timeSlotService;
    private SlotConfiguration slotConfiguration;


    public TimeSlotResource(TimeSlotService timeSlotService, SlotConfiguration slotConfiguration) {
        this.timeSlotService = timeSlotService;
        this.slotConfiguration = slotConfiguration;
    }


    /**
     * POST  /initialize : Generate TimeSlots for the days .
     *
     * @param slotDays the list of days
     * @return the ResponseEntity with status 201 (Created) and with body the List of, or with status 400 (Bad Request)
     * if the days are not in valid period
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/initializeCalendar")
    public ResponseEntity createStylist(@Valid @RequestBody @DateTimeFormat(pattern = "YYYYMMDD") List<LocalDate> slotDays) throws URISyntaxException {
        log.info("REST request to save slot days : {}", slotDays.size());

        ResponseEntity response = RuleValidator.checkRules(slotDays);
        if (response != null) {
            return response;
        }
        List<TimeSlot> timeSlots = slotDays.stream().map(day -> {
            List<TimeSlot> slotsInDay = new ArrayList<>();
            for (int i = 0; i < slotConfiguration.getNumberOfSlots(); i++) {
                TimeSlotPK timeSlotPK = new TimeSlotPK(i, day);
                TimeSlot timeSlot = new TimeSlot(timeSlotPK, INITIAL_APPOINTMENT_COUNT, INITIAL_VERSION);
                slotsInDay.add(timeSlot);
            }
            return slotsInDay;

        }).flatMap(List::stream).collect(Collectors.toList());

        List<TimeSlotPK> timeSlotPKS = timeSlots.stream()
            .map(timeSlot -> timeSlot.getId())
            .collect(Collectors.toList());
        Boolean alreadyInitialized = timeSlotService.checkIfAlreadyExist(timeSlotPKS);

        if (alreadyInitialized) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Already Initialized dates exist");
        }
        List<TimeSlot> result = timeSlotService.saveAll(timeSlots);
        return new ResponseEntity(result.stream().map(timeSlot ->
            new TimeSlotDto(Constants.getTime(slotConfiguration, timeSlot, 0), Constants.getTime(slotConfiguration, timeSlot, 1))
        ).collect(Collectors.toList()), HttpStatus.OK);
    }


    /**
     * GET  / : get all available slots.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of customer data in body
     */
    @GetMapping("/availableSlots")
    @Timed
    public List<TimeSlotDto> getAvailableSlots() {
        log.info("REST request to get all AvailableSlots");
        return timeSlotService.findAvailableSlots().stream()
            .map(timeSlot -> new TimeSlotDto(
                Constants.getTime(slotConfiguration, timeSlot, 0),
                Constants.getTime(slotConfiguration, timeSlot, 1)
            )).collect(Collectors.toList());
    }


}
