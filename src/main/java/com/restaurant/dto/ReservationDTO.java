// src/main/java/com/restaurant/dto/ReservationDTO.java
package com.restaurant.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationDTO {
    private Long id;
    private Long customerId;
    private Long tableId;
    private LocalDateTime reservationTime;
    private int durationMinutes;
    private String status;
    private int numGuests; 
}