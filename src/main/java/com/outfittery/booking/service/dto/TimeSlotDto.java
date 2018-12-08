package com.outfittery.booking.service.dto;

import com.outfittery.booking.config.SlotConfiguration;
import com.outfittery.booking.config.util.Constants;
import com.outfittery.booking.domain.TimeSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlotDto {

    LocalDateTime startTime;
    LocalDateTime endTime;

}
