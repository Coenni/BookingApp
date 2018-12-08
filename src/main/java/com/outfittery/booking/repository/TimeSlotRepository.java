package com.outfittery.booking.repository;

import com.outfittery.booking.domain.Stylist;
import com.outfittery.booking.domain.TimeSlot;
import com.outfittery.booking.domain.embeddedids.TimeSlotPK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the TimeSlot entity.
 */
@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, TimeSlotPK> {


    List<TimeSlot> findByAppointmentCounterLessThan(Long numberOfStylists);

    @Query( "select ts from TimeSlot ts where id in :ids" )
    List findByIdIn(@Param("ids") List<TimeSlotPK> timeSlotPKS);
}
