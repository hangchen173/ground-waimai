package com.restaurant.service;

import com.restaurant.dto.TableDTO;
import com.restaurant.entity.Restaurant;
import com.restaurant.entity.RestaurantTable;
import com.restaurant.exception.BadRequestException;
import com.restaurant.repository.RestaurantRepository;
import com.restaurant.repository.TableRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private TableRepository tableRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private TableService tableService;

    @Test
    void createTable_WhenRestaurantExists_ShouldSuccess() {
        TableDTO dto = new TableDTO();
        dto.setRestaurantId(1L);
        dto.setTableNumber(5);
        dto.setCapacity(4);

        Restaurant restaurant = new Restaurant(); restaurant.setId(1L);
        RestaurantTable  savedTable = new RestaurantTable (); 
        savedTable.setId(10L); 
        savedTable.setRestaurant(restaurant);
        savedTable.setTableNumber(5);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(tableRepository.save(any(RestaurantTable.class))).thenReturn(savedTable);

        TableDTO result = tableService.createTable(dto);

        assertEquals(10L, result.getId());
        assertEquals(1L, result.getRestaurantId());
    }

    @Test
    void createTable_WithoutRestaurantId_ShouldThrowBadRequest() {
        TableDTO dto = new TableDTO();
        // restaurantId is null

        assertThrows(BadRequestException.class, () -> tableService.createTable(dto));
    }

    @Test
    void updateTable_ShouldUpdateFields() {
        RestaurantTable  existing = new  RestaurantTable(); existing.setId(1L); existing.setCapacity(2);
        // Note: restaurant needs to be set because convertToDTO calls getRestaurant().getId()
        Restaurant r = new Restaurant(); r.setId(5L);
        existing.setRestaurant(r);

        TableDTO dto = new TableDTO(); dto.setCapacity(6); dto.setRestaurantId(null); // No restaurant change

        when(tableRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(tableRepository.save(any(RestaurantTable .class))).thenAnswer(i -> i.getArgument(0));

        TableDTO result = tableService.updateTable(1L, dto);

        assertEquals(6, result.getCapacity());
        assertEquals(5L, result.getRestaurantId()); // Should remain unchanged
    }
}