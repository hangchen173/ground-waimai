// src/main/java/com/restaurant/controller/TableController.java
package com.restaurant.controller;

import com.restaurant.dto.RestaurantTableDTO;
import com.restaurant.service.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
public class RestaurantTableController {

    @Autowired
    private RestaurantTableService tableService;

    @GetMapping
    public ResponseEntity<List<RestaurantTableDTO>> getAllTables() {
        return ResponseEntity.ok(tableService.getAllTables());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantTableDTO> getTableById(@PathVariable Long id) {
        return ResponseEntity.ok(tableService.getTableById(id));
    }

    @PostMapping
    public ResponseEntity<RestaurantTableDTO> createTable(@RequestBody RestaurantTableDTO tableDTO) {
        return new ResponseEntity<>(tableService.createTable(tableDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantTableDTO> updateTable(@PathVariable Long id, @RequestBody RestaurantTableDTO tableDTO) {
        return ResponseEntity.ok(tableService.updateTable(id, tableDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable Long id) {
        tableService.deleteTable(id);
        return ResponseEntity.noContent().build();
    }
}