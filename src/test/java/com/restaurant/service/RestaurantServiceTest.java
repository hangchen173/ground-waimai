package com.restaurant.service;

import com.restaurant.dto.RestaurantDTO;
import com.restaurant.entity.Restaurant;
import com.restaurant.exception.ResourceNotFoundException;
import com.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    @Test
    void getRestaurantById_ShouldReturnDTO() {
        Restaurant r = new Restaurant(); r.setId(1L); r.setName("Tasty Food");
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(r));

        RestaurantDTO result = restaurantService.getRestaurantById(1L);

        assertEquals("Tasty Food", result.getName());
    }

    @Test
    void createRestaurant_ShouldReturnDTO() {
        RestaurantDTO dto = new RestaurantDTO(); dto.setName("Burger King");
        Restaurant saved = new Restaurant(); saved.setId(1L); saved.setName("Burger King");

        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(saved);

        RestaurantDTO result = restaurantService.createRestaurant(dto);

        assertNotNull(result.getId());
        assertEquals("Burger King", result.getName());
    }

    @Test
    void deleteRestaurant_WhenNotExists_ShouldThrowException() {
        when(restaurantRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> restaurantService.deleteRestaurant(99L));
        verify(restaurantRepository, never()).deleteById(any());
    }
}