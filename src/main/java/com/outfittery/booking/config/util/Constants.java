package com.outfittery.booking.config.util;

import com.outfittery.booking.config.SlotConfiguration;
import com.outfittery.booking.domain.TimeSlot;

import java.time.LocalDateTime;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String NAME_REGEX = "^[_.@A-Za-z0-9-]*$";
    public static final String PROFILE_DEVELOPMENT = "dev";

    private Constants() {
    }


    public static LocalDateTime getTime(SlotConfiguration slotConfiguration, TimeSlot timeSlot, Integer incrementalValue) {
        return timeSlot.getId().getDate().atStartOfDay()
            .plusHours(slotConfiguration.getStartHour())
            .plusMinutes(slotConfiguration.getSlotDuration() * (timeSlot.getId().getSlotIndex() + incrementalValue));
    }
}
