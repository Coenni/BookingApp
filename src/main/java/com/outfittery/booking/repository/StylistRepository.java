package com.outfittery.booking.repository;

import com.outfittery.booking.domain.Stylist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Stylist entity.
 */
@Repository
public interface StylistRepository extends JpaRepository<Stylist, Long> {
}
