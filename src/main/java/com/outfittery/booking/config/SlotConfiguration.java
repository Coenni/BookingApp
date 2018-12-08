package com.outfittery.booking.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@Getter
@Setter
public class SlotConfiguration {


    private Integer startHour;

    private Integer slotDuration;

    private Integer numberOfSlots;


    public SlotConfiguration(@Value("${appointment.startHour}")Integer startHour,
                             @Value("${appointment.duration}") Integer slotDuration,
                             @Value("${appointment.perDay}")Integer numberOfSlots) {
        this.startHour = startHour;
        this.slotDuration = slotDuration;
        this.numberOfSlots = numberOfSlots;
    }
}
