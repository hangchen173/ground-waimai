// src/main/java/com/restaurant/util/DateTimeUtils.java
package com.restaurant.util;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateTimeUtils {

    private static final LocalTime OPEN_TIME = LocalTime.of(9, 0);
    private static final LocalTime CLOSE_TIME = LocalTime.of(22, 0);
    private static final int MIN_DURATION = 30;
    private static final int MAX_DURATION = 120;

    public static boolean isValidTimeSlot(LocalDateTime startTime, int durationMinutes) {
        LocalDateTime now = LocalDateTime.now();
        if (startTime.isBefore(now)) {
            return false; 
        }
        LocalTime start = startTime.toLocalTime();
        LocalTime end = start.plusMinutes(durationMinutes);

        if (start.isBefore(OPEN_TIME) || end.isAfter(CLOSE_TIME)) {
            return false;
        }

        if (durationMinutes < MIN_DURATION || durationMinutes > MAX_DURATION) {
            return false;
        }
        // Additional checks, e.g., round to nearest 15 min, etc.
        return true;
    }
}