package com.outfittery.booking.domain.embeddedids;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Getter
public class TimeSlotPK implements Serializable {
    Integer slotIndex;
    LocalDate date;
}