package com.outfittery.booking.domain;

import com.outfittery.booking.domain.embeddedids.TimeSlotPK;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "time_slot")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlot {
    @EmbeddedId
    TimeSlotPK id;

    @Column(name = "appointment_counter")
    Integer appointmentCounter = 0;

    @Version
    private Integer version;

}

