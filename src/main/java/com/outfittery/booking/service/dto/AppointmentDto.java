package com.outfittery.booking.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.outfittery.booking.config.SlotConfiguration;
import com.outfittery.booking.config.util.Constants;
import com.outfittery.booking.domain.TimeSlot;
import com.outfittery.booking.domain.embeddedids.TimeSlotPK;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class AppointmentDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long customerId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long stylistId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    TimeSlotPK timeSlotPK;

    String customerName;
    String stylistName;
    TimeSlotDto timeSlot;

    public AppointmentDto( Long customerId, Long stylistId, TimeSlotPK timeSlotPK){
        this.customerId = customerId;
        this.stylistId = stylistId;
        this.timeSlotPK = timeSlotPK;
    }

    public AppointmentDto( String  customerName, String stylistName, TimeSlotDto timeSlot){
        this.customerName = customerName;
        this.stylistName = stylistName;
        this.timeSlot = timeSlot;
    }



}
