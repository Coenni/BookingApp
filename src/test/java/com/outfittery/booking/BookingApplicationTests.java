package com.outfittery.booking;

import com.outfittery.booking.repository.CustomerRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookingApplicationTests {

	@Autowired
	private CustomerRepository customerRepository;

	@Test
	public void contextLoads() {

	}

}
