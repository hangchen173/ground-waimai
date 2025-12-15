package com.restaurant.repository;

import com.restaurant.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Finds reservations for the given table that start before the specified endTime.
     * Further overlap checks are performed in the service layer.
     */
    // ✅ 修复：将 r.table.id 改为 r.restaurantTable.id
    @Query("SELECT r FROM Reservation r WHERE r.restaurantTable.id = :tableId AND r.reservationTime < :endTime")
    List<Reservation> findPotentialOverlaps(@Param("tableId") Long tableId,
                                            @Param("endTime") LocalDateTime endTime);
}