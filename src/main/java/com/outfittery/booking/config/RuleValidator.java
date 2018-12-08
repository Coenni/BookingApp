package com.outfittery.booking.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

public class RuleValidator {

    public static ResponseEntity checkRules(List<LocalDate> slotDays){
        Set<LocalDate> slotDaysWithoutDuplicates = slotDays.stream().collect(Collectors.toSet());

        if (slotDaysWithoutDuplicates.size() == 0) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("List is empty");
        }

        if (slotDaysWithoutDuplicates.size() < slotDays.size()) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("List is not valid");
        }

        slotDays.sort((o1, o2) -> o1.compareTo(o2));

        LocalDate now = LocalDate.now();
        if (slotDays.get(0).isBefore(now)) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("List is not valid, including already passed days");
        }

        if (DAYS.between(slotDays.get(slotDays.size() - 1), now) > 30) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body( "Next 30 days are available");
        }
        return null;
    }
}
