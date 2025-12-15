// src/main/java/com/restaurant/service/TableService.java
package com.restaurant.service;

import com.restaurant.dto.TableDTO;
import com.restaurant.entity.Restaurant;
import com.restaurant.entity.RestaurantTable;
import com.restaurant.exception.BadRequestException;
import com.restaurant.exception.ResourceNotFoundException;
import com.restaurant.repository.RestaurantRepository;
import com.restaurant.repository.TableRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    /*---------------------- Public Methods ----------------------*/

    public List<TableDTO> getAllTables() {
        return tableRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TableDTO getTableById(Long id) {
        RestaurantTable table = getTableOrThrow(id);
        return convertToDTO(table);
    }

    public TableDTO createTable(TableDTO tableDTO) {
        validateCreateInput(tableDTO);

        Restaurant restaurant =
                getRestaurantOrThrow(tableDTO.getRestaurantId());

        RestaurantTable table = convertToEntity(tableDTO);
        table.setRestaurant(restaurant);

        return convertToDTO(tableRepository.save(table));
    }

    public TableDTO updateTable(Long id, TableDTO tableDTO) {
        RestaurantTable table = getTableOrThrow(id);
        updateTableFields(table, tableDTO);
        return convertToDTO(tableRepository.save(table));
    }

    public void deleteTable(Long id) {
        if (!tableRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Table not found with id " + id
            );
        }
        tableRepository.deleteById(id);
    }

    /*---------------------- Private Helpers ----------------------*/

    private RestaurantTable getTableOrThrow(Long id) {
        return tableRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Table not found with id " + id
                        ));
    }

    private Restaurant getRestaurantOrThrow(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Restaurant not found with id " + id
                        ));
    }

    private void validateCreateInput(TableDTO dto) {
        if (dto.getRestaurantId() == null) {
            throw new BadRequestException("restaurantId is required");
        }
    }

    private void updateTableFields(RestaurantTable table, TableDTO dto) {
        if (dto.getRestaurantId() != null) {
            table.setRestaurant(
                    getRestaurantOrThrow(dto.getRestaurantId())
            );
        }
        table.setTableNumber(dto.getTableNumber());
        table.setCapacity(dto.getCapacity());
        table.setAvailable(dto.isAvailable());
    }

    private TableDTO convertToDTO(RestaurantTable table) {
        TableDTO dto = new TableDTO();
        dto.setId(table.getId());
        dto.setRestaurantId(table.getRestaurant().getId());
        dto.setTableNumber(table.getTableNumber());
        dto.setCapacity(table.getCapacity());
        dto.setAvailable(table.isAvailable());
        return dto;
    }

    private RestaurantTable convertToEntity(TableDTO dto) {
        RestaurantTable table = new RestaurantTable();
        table.setTableNumber(dto.getTableNumber());
        table.setCapacity(dto.getCapacity());
        table.setAvailable(dto.isAvailable());
        return table;
    }
}
