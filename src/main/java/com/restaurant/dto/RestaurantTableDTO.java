// src/main/java/com/restaurant/dto/RestaurantTableDTO.java
package com.restaurant.dto;

import lombok.Data;

@Data
public class RestaurantTableDTO {
    private Long id;
    private Long restaurantId;
    private int tableNumber;
    private int capacity;
    private boolean available;
}