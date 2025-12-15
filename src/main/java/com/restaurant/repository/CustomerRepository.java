// src/main/java/com/restaurant/repository/CustomerRepository.java
package com.restaurant.repository;

import com.restaurant.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}