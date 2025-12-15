package com.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

// ✅ 引入正确的类
import com.restaurant.entity.RestaurantTable; 

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String phone;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    @ToString.Exclude 
    // ✅ 修改泛型类型为 RestaurantTable
    private List<RestaurantTable> tables; 
}