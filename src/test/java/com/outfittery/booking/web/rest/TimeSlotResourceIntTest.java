package com.outfittery.booking.web.rest;


import com.outfittery.booking.BookingApplication;
import com.outfittery.booking.config.SlotConfiguration;
import com.outfittery.booking.domain.Customer;
import com.outfittery.booking.domain.TimeSlot;
import com.outfittery.booking.repository.CustomerRepository;
import com.outfittery.booking.service.TimeSlotService;
import com.outfittery.booking.service.dto.CustomerDto;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;

import static com.outfittery.booking.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the CustomerResource REST controller.
 *
 * @see CustomerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookingApplication.class)
public class TimeSlotResourceIntTest {

    private static final Long DEFAULT_ID = 1l;
    private static final Long SECOND_ID = 2l;

    private static final String DEFAULT_NAME = "AAA";
    private static final String UPDATE_NAME = "BBB";
    private static final String CUSTOMER_NAME_FOR_POST_CALL = "BBB";

    @Autowired
    private TimeSlotService timeSlotService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTimeSlotMockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SlotConfiguration slotConfiguration = new SlotConfiguration(9,30, 16);
        final TimeSlotResource timeSlotResource = new TimeSlotResource(timeSlotService, slotConfiguration);
        this.restTimeSlotMockMvc = MockMvcBuilders.standaloneSetup(timeSlotResource)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Test
    public void createValidTimeSlots() throws Exception {
        List<LocalDate> inputDates = new ArrayList<>();
        IntStream.of(1, 2, 3, 4, 5).forEach( day-> inputDates.add(LocalDate.now().plusDays(day)));
        this.restTimeSlotMockMvc.perform(post("/api/initializeCalendar")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inputDates)))
            .andExpect(status().isOk());

    }

    @Test
    public void createInvalidTimeSlots() throws Exception {
        List<LocalDate> inputDates = new ArrayList<>();
        IntStream.of(1, 2, 3).forEach( day-> inputDates.add(LocalDate.now().minusDays(day)));
        this.restTimeSlotMockMvc.perform(post("/api/initializeCalendar")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inputDates)))
            .andExpect(status().isBadRequest());


    }


}
