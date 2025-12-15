// src/main/java/com/restaurant/dto/TableDTO.java
package com.restaurant.dto;

import lombok.Data;

@Data
public class TableDTO {
    private Long id;
    private Long restaurantId;
    private int tableNumber;
    private int capacity;
    private boolean available;
}