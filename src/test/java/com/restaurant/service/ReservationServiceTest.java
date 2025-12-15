package com.restaurant.service;

import com.restaurant.dto.ReservationDTO;
import com.restaurant.entity.Customer;
import com.restaurant.entity.Reservation;
import com.restaurant.entity.RestaurantTable;
import com.restaurant.exception.BadRequestException;
import com.restaurant.exception.ConflictException;
import com.restaurant.repository.CustomerRepository;
import com.restaurant.repository.ReservationRepository;
import com.restaurant.repository.TableRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private TableRepository tableRepository;

    @InjectMocks
    private ReservationService reservationService;

    // ---------------------- SUCCESS TEST ----------------------
    @Test
    void createReservation_Success() {

        LocalDateTime bookingTime = LocalDateTime.now().plusDays(1)
                .withHour(18).withMinute(0);

        ReservationDTO dto = new ReservationDTO();
        dto.setCustomerId(1L);
        dto.setTableId(10L);
        dto.setReservationTime(bookingTime);
        dto.setDurationMinutes(60);
        dto.setNumGuests(2);

        Customer customer = new Customer();
        customer.setId(1L);

        RestaurantTable table = new RestaurantTable();
        table.setId(10L);
        table.setCapacity(4);

        Reservation saved = new Reservation();
        saved.setId(100L);
        saved.setCustomer(customer);
        saved.setRestaurantTable(table);
        saved.setReservationTime(bookingTime);
        saved.setDurationMinutes(60);
        saved.setStatus("CONFIRMED");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(tableRepository.findById(10L)).thenReturn(Optional.of(table));
        when(reservationRepository.findPotentialOverlaps(anyLong(), any()))
                .thenReturn(Collections.emptyList());
        when(reservationRepository.save(any(Reservation.class)))
                .thenReturn(saved);

        ReservationDTO result = reservationService.createReservation(dto);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("CONFIRMED", result.getStatus());
    }

    // ---------------------- BAD REQUEST TEST ----------------------
    @Test
    void createReservation_GuestCapacityExceeded_ThrowsBadRequest() {

        ReservationDTO dto = new ReservationDTO();
        dto.setCustomerId(1L);
        dto.setTableId(10L);
        dto.setReservationTime(LocalDateTime.now().plusDays(1));
        dto.setNumGuests(10); // 超过 4 人桌

        Customer customer = new Customer();
        RestaurantTable table = new RestaurantTable();
        table.setCapacity(4);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(tableRepository.findById(10L)).thenReturn(Optional.of(table));

        assertThrows(BadRequestException.class,
                () -> reservationService.createReservation(dto));
    }

    // ---------------------- CONFLICT TEST ----------------------
    @Test
    void createReservation_TimeConflict_ThrowsConflictException() {

        LocalDateTime bookingTime = LocalDateTime.now().plusDays(1)
                .withHour(12).withMinute(0);

        ReservationDTO dto = new ReservationDTO();
        dto.setCustomerId(1L);
        dto.setTableId(10L);
        dto.setReservationTime(bookingTime);
        dto.setDurationMinutes(60);
        dto.setNumGuests(2);

        Customer customer = new Customer();

        RestaurantTable table = new RestaurantTable();
        table.setId(10L);
        table.setCapacity(4);

        // 模拟冲突预订
        Reservation conflict = new Reservation();
        conflict.setId(99L);
        conflict.setReservationTime(bookingTime.minusMinutes(30));
        conflict.setDurationMinutes(60);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(tableRepository.findById(10L)).thenReturn(Optional.of(table));
        when(reservationRepository.findPotentialOverlaps(anyLong(), any()))
                .thenReturn(List.of(conflict));

        // 必须调用 service，否则 Mockito 认为 stub 没用
        assertThrows(ConflictException.class,
                () -> reservationService.createReservation(dto));
    }
}
