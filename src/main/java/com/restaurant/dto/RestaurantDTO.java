// src/main/java/com/restaurant/dto/RestaurantDTO.java
package com.restaurant.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class RestaurantDTO {

    private Long id;

    @NotBlank(message = "Restaurant name is required.")
    private String name;

    @NotBlank(message = "Restaurant address is required.")
    private String address;
    @NotBlank(message = "Restaurant phone is required.")
    private String phone;
}
