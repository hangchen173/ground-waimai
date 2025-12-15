package com.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Table(name = "restaurant_table")
public class RestaurantTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 建议加上 LAZY，提高性能
    @JoinColumn(name = "restaurant_id")
    @ToString.Exclude // 🚨 关键：防止死循环
    private Restaurant restaurant;

    private int tableNumber;
    private int capacity;
    private boolean available;
}