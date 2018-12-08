package com.outfittery.booking.web.rest;


import com.outfittery.booking.BookingApplication;
import com.outfittery.booking.config.SlotConfiguration;
import com.outfittery.booking.domain.Customer;
import com.outfittery.booking.domain.Stylist;
import com.outfittery.booking.domain.TimeSlot;
import com.outfittery.booking.domain.embeddedids.TimeSlotPK;
import com.outfittery.booking.service.AppointmentService;
import com.outfittery.booking.service.CustomerService;
import com.outfittery.booking.service.StylistService;
import com.outfittery.booking.service.TimeSlotService;
import com.outfittery.booking.service.dto.AppointmentDto;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static com.outfittery.booking.web.rest.TestUtil.createFormattingConversionService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the AppointmentResource REST controller.
 *
 * @see AppointmentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookingApplication.class)
public class AppointmentResourceIntTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private StylistService stylistService;

    @Autowired
    private TimeSlotService timeSlotService;

    @Autowired
    private AppointmentService appointmentService;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;


    private MockMvc restAppointmentMockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SlotConfiguration slotConfiguration = new SlotConfiguration(9,30, 16);
        final AppointmentResource timeSlotResource = new AppointmentResource(appointmentService, customerService, stylistService, slotConfiguration, timeSlotService);
        this.restAppointmentMockMvc = MockMvcBuilders.standaloneSetup(timeSlotResource)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Test
    public void makeAppointment() throws Exception {

        Customer customerFirst = customerService.save(new Customer(null, "FirstCustomer"));
        Customer customerSecond = customerService.save(new Customer(null, "SecondCustomer"));
        Customer customerThird = customerService.save(new Customer(null, "ThirdCustomer"));
        Stylist stylistFirst = stylistService.save(new Stylist(null, "FirstStylist"));
        Stylist stylistSecond = stylistService.save(new Stylist(null, "SecondStylist"));
        TimeSlot timeSlot = timeSlotService.save(new TimeSlot(new TimeSlotPK(0, LocalDate.now()), 0, 1));

        AppointmentDto appointmentDtoFirst = new AppointmentDto(customerFirst.getId(), null, timeSlot.getId());
        AppointmentDto appointmentDtoSecond = new AppointmentDto(customerSecond.getId(), null, timeSlot.getId());
        AppointmentDto appointmentDtoThird = new AppointmentDto(customerThird.getId(), null, timeSlot.getId());

        this.restAppointmentMockMvc.perform(post("/api/makeAppointment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDtoFirst)))
            .andExpect(status().isCreated());


        this.restAppointmentMockMvc.perform(post("/api/makeAppointment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDtoSecond)))
            .andExpect(status().isCreated());

        // the third customer for that slot response : NO_CONTENT
        this.restAppointmentMockMvc.perform(post("/api/makeAppointment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDtoThird)))
            .andExpect(status().isNoContent());

        this.restAppointmentMockMvc.perform(get("/api/appointment")).andDo(MockMvcResultHandlers.print());


    }


}
