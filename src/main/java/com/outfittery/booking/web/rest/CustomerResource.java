package com.outfittery.booking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.outfittery.booking.config.util.Constants;
import com.outfittery.booking.config.util.HeaderUtil;
import com.outfittery.booking.domain.Appointment;
import com.outfittery.booking.domain.Customer;
import com.outfittery.booking.repository.CustomerRepository;
import com.outfittery.booking.service.CustomerService;
import com.outfittery.booking.service.dto.AppointmentDto;
import com.outfittery.booking.service.dto.CustomerDto;
import com.outfittery.booking.service.dto.TimeSlotDto;
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
 * REST controller for managing Customer.
 */
@RestController
@RequestMapping("/api")
public class CustomerResource {

    private final Logger log = LoggerFactory.getLogger(CustomerResource.class);

    private static final String ENTITY_NAME = "customer";

    private final CustomerService customerService;


    public CustomerResource(CustomerService customerService) {
        this.customerService = customerService;
    }


    /**
     * POST  /customer : Create a new customer.
     *
     * @param customerDto the customer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customer, or with status 400 (Bad Request) if the customer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/customer")
    @Timed
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody CustomerDto customerDto) throws URISyntaxException {
        log.info( "REST request to save Customer : {}", customerDto);
        if (customerDto.getCustomerId() != null) {
            throw new BadRequestAlertException("A new customer cannot already have an ID", ENTITY_NAME, ID_EXISTS);
        }
        Customer result = customerService.save(this.convertToEntity(customerDto));
        return ResponseEntity.created(new URI("/api/customer/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /customer : get all the customer.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of customer data in body
     */
    @GetMapping("/customer")
    @Timed
    public List<CustomerDto> getAllCustomer() {
        log.info( "REST request to get all Customer");
        return customerService.findAll().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    /**
     * GET  /customer/:id : get the "id" customer.
     *
     * @param id the id of the customer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customer data, or with status 404 (Not Found)
     */
    @GetMapping("/customer/{id}")
    @Timed
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable Long id) {
        log.info( "REST request to get Customer : {}", id);
        Optional<Customer> customer = customerService.findOne(id);

        return customer.map((Customer response) -> {
            return (ResponseEntity.ok()).body(this.convertToDto(response));
        }).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
    }


    private CustomerDto convertToDto(Customer customer) {
        return new CustomerDto(customer.getId(), customer.getName());
    }

    private Customer convertToEntity(CustomerDto customerDto) {
        return new Customer(customerDto.getCustomerId(), customerDto.getCustomerName());
    }
}
