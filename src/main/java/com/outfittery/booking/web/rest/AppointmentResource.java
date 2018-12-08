package com.outfittery.booking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.outfittery.booking.config.SlotConfiguration;
import com.outfittery.booking.config.util.Constants;
import com.outfittery.booking.config.util.HeaderUtil;
import com.outfittery.booking.domain.Appointment;
import com.outfittery.booking.domain.Customer;
import com.outfittery.booking.domain.Stylist;
import com.outfittery.booking.domain.TimeSlot;
import com.outfittery.booking.service.AppointmentService;
import com.outfittery.booking.service.CustomerService;
import com.outfittery.booking.service.StylistService;
import com.outfittery.booking.service.TimeSlotService;
import com.outfittery.booking.service.dto.AppointmentDto;
import com.outfittery.booking.service.dto.TimeSlotDto;
import com.outfittery.booking.web.rest.errors.ErrorConstants;

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


/**
 * REST controller for managing Appointment.
 */
@RestController
@RequestMapping("/api")
public class AppointmentResource {

    private final Logger log = LoggerFactory.getLogger(AppointmentResource.class);

    private static final String ENTITY_NAME = "appointment";

    private final AppointmentService appointmentService;

    private final CustomerService customerService;

    private final StylistService stylistService;

    private final SlotConfiguration slotConfiguration;

    private final TimeSlotService timeSlotService;


    public AppointmentResource(AppointmentService appointmentService, CustomerService customerService, StylistService stylistService, SlotConfiguration slotConfiguration, TimeSlotService timeSlotService) {
        this.appointmentService = appointmentService;
        this.customerService = customerService;
        this.stylistService = stylistService;
        this.slotConfiguration = slotConfiguration;
        this.timeSlotService = timeSlotService;
    }




    /**
     * POST  /appointment : Create a new appointment.
     *
     * @param appointmentDto the appointment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new appointment, or with status 400 (Bad Request) if the appointment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/makeAppointment")
    @Timed
    public ResponseEntity<Appointment> createAppointment(@Valid @RequestBody AppointmentDto appointmentDto) throws URISyntaxException {
        log.info( "REST request to save Appointment : {}", appointmentDto);

        Optional<TimeSlot> timeSlotOptional = timeSlotService.findOne(appointmentDto.getTimeSlotPK());

        if(!timeSlotOptional.isPresent()){
            return ResponseEntity.noContent()
                .headers(HeaderUtil.createAlert(ENTITY_NAME, ErrorConstants.NO_TIME_SLOT_FOUND))
                .build();
        }

        Optional<Customer> customerOptional = customerService.findOne(appointmentDto.getCustomerId());
        if(!customerOptional.isPresent()){
            return ResponseEntity.noContent()
                .headers(HeaderUtil.createAlert(ENTITY_NAME, ErrorConstants.NO_CUSTOMER_FOUND))
                .build();
        }

        Stylist stylist = appointmentService.findAvailableStylist(timeSlotOptional.get());

        if(stylist == null ){
            return ResponseEntity.noContent()
                .headers(HeaderUtil.createAlert(ENTITY_NAME, ErrorConstants.NO_CONTENT))
                .build();
        }

        Appointment result = appointmentService.save(
            new Appointment(null, stylist, customerOptional.get(), timeSlotOptional.get()));
        return ResponseEntity.created(new URI("/api/appointment/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /appointment : get all the appointment.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of appointment data in body
     */
    @GetMapping("/appointment")
    @Timed
    public List<AppointmentDto> getAllAppointment() {
        log.info( "REST request to get all Appointment");
        return appointmentService.findAll().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    /**
     * GET  /appointment/:id : get the "id" appointment.
     *
     * @param id the id of the appointment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the appointment data, or with status 404 (Not Found)
     */
    @GetMapping("/appointment/{id}")
    @Timed
    public ResponseEntity<AppointmentDto> getAppointment(@PathVariable Long id) {
        log.info( "REST request to get Appointment : {}", id);
        Optional<Appointment> appointment = appointmentService.findOne(id);

        return appointment.map((Appointment response) -> {
            return (ResponseEntity.ok()).body(this.convertToDto(response));
        }).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
    }


    private AppointmentDto convertToDto(Appointment appointment) {
        return new AppointmentDto(
            appointment.getCustomer().getName(),
            appointment.getStylist().getName(),
            new TimeSlotDto(
                Constants.getTime(slotConfiguration, appointment.getTimeSlot(), 0),
                Constants.getTime(slotConfiguration, appointment.getTimeSlot(), 1))
        );
    }
}
