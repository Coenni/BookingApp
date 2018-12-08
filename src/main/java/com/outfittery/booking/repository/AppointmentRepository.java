package com.outfittery.booking.repository;

import com.outfittery.booking.domain.Appointment;
import com.outfittery.booking.domain.Stylist;
import com.outfittery.booking.domain.TimeSlot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Appointment entity.
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("from Stylist as st where st not in (select ap.stylist from Appointment ap where ap.timeSlot=:timeSlot)")
    List<Stylist> findAvailableStylist(@Param("timeSlot") TimeSlot timeSlot);
}
