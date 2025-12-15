package com.restaurant.service;

import com.restaurant.dto.ReservationDTO;
import com.restaurant.entity.Customer;
import com.restaurant.entity.Reservation;
import com.restaurant.entity.RestaurantTable;
import com.restaurant.exception.BadRequestException;
import com.restaurant.exception.ConflictException;
import com.restaurant.exception.ResourceNotFoundException;
import com.restaurant.repository.CustomerRepository;
import com.restaurant.repository.ReservationRepository;
import com.restaurant.repository.TableRepository;
import com.restaurant.util.DateTimeUtils;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final TableRepository tableRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              CustomerRepository customerRepository,
                              TableRepository tableRepository) {
        this.reservationRepository = reservationRepository;
        this.customerRepository = customerRepository;
        this.tableRepository = tableRepository;
    }

    /* -------------------- PUBLIC API -------------------- */

    public List<ReservationDTO> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ReservationDTO getReservationById(Long id) {
        return convertToDTO(findReservation(id));
    }

    public ReservationDTO createReservation(ReservationDTO dto) {
        validateCreateFields(dto);

        Customer customer = findCustomer(dto.getCustomerId());
        RestaurantTable table = findRestaurantTable(dto.getTableId());

        validateGuestCount(dto.getNumGuests(), table.getCapacity());

        // 先业务冲突（409），再结构校验（400）
        checkTimeConflict(
                table.getId(),
                dto.getReservationTime(),
                dto.getDurationMinutes(),
                null
        );
        validateTimeSlot(dto.getReservationTime(), dto.getDurationMinutes());

        Reservation reservation = convertToEntity(dto);
        reservation.setCustomer(customer);
        reservation.setRestaurantTable(table);
        reservation.setStatus("CONFIRMED");

        return convertToDTO(reservationRepository.save(reservation));
    }

    public ReservationDTO updateReservation(Long id, ReservationDTO dto) {
        Reservation existing = findReservation(id);

        if (dto.getTableId() != null) {
            RestaurantTable newTable = findRestaurantTable(dto.getTableId());
            existing.setRestaurantTable(newTable);
        }

        updateTimeIfChanged(existing, dto);
        updateDurationIfPresent(existing, dto);
        updateStatusIfPresent(existing, dto);

        return convertToDTO(reservationRepository.save(existing));
    }

    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation not found with id " + id);
        }
        reservationRepository.deleteById(id);
    }

    /* -------------------- VALIDATION HELPERS -------------------- */

    private void validateCreateFields(ReservationDTO dto) {
        if (dto.getCustomerId() == null ||
            dto.getTableId() == null ||
            dto.getReservationTime() == null) {
            throw new BadRequestException(
                    "customerId, tableId, and reservationTime are required."
            );
        }
    }

    private void validateGuestCount(int guests, int capacity) {
        if (guests > capacity) {
            throw new BadRequestException(
                    "Number of guests exceeds table capacity."
            );
        }
    }

    private void validateTimeSlot(LocalDateTime startTime, int durationMinutes) {
        if (startTime == null) {
            throw new BadRequestException("reservationTime is required.");
        }
        if (durationMinutes <= 0) {
            throw new BadRequestException("durationMinutes must be greater than 0.");
        }
        if (!DateTimeUtils.isValidTimeSlot(startTime, durationMinutes)) {
            throw new BadRequestException("Invalid reservation time slot.");
        }
    }

    /**
     * Check for booking conflicts (double booking).
     * Throws ConflictException (HTTP 409) if overlaps exist.
     */
    private void checkTimeConflict(Long tableId,
                                   LocalDateTime start,
                                   int duration,
                                   Long excludeReservationId) {

        if (start == null) return;

        LocalDateTime end = start.plusMinutes(duration);

        List<Reservation> overlaps = reservationRepository
                .findPotentialOverlaps(tableId, end)
                .stream()
                .filter(r ->
                        excludeReservationId == null ||
                        !r.getId().equals(excludeReservationId))
                .filter(r ->
                        r.getReservationTime()
                         .plusMinutes(r.getDurationMinutes())
                         .isAfter(start))
                .collect(Collectors.toList());

        if (!overlaps.isEmpty()) {
            throw new ConflictException(
                    "Table is already reserved for the requested time slot."
            );
        }
    }

    private void updateTimeIfChanged(Reservation existing, ReservationDTO dto) {
        if (dto.getReservationTime() == null) return;

        int durationToCheck =
                dto.getDurationMinutes() > 0
                        ? dto.getDurationMinutes()
                        : existing.getDurationMinutes();

        checkTimeConflict(
                existing.getRestaurantTable().getId(),
                dto.getReservationTime(),
                durationToCheck,
                existing.getId()
        );

        validateTimeSlot(dto.getReservationTime(), durationToCheck);

        existing.setReservationTime(dto.getReservationTime());
    }

    private void updateDurationIfPresent(Reservation existing, ReservationDTO dto) {
        if (dto.getDurationMinutes() > 0) {
            checkTimeConflict(
                    existing.getRestaurantTable().getId(),
                    existing.getReservationTime(),
                    dto.getDurationMinutes(),
                    existing.getId()
            );

            validateTimeSlot(
                    existing.getReservationTime(),
                    dto.getDurationMinutes()
            );

            existing.setDurationMinutes(dto.getDurationMinutes());
        }
    }

    private void updateStatusIfPresent(Reservation existing, ReservationDTO dto) {
        if (dto.getStatus() != null) {
            existing.setStatus(dto.getStatus());
        }
    }

    /* -------------------- ENTITY HELPERS -------------------- */

    private Reservation findReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Reservation not found with id " + id
                        ));
    }

    private Customer findCustomer(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found with id " + id
                        ));
    }

    private RestaurantTable findRestaurantTable(Long id) {
        return tableRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Table not found with id " + id
                        ));
    }

    /* -------------------- DTO CONVERTERS -------------------- */

    private ReservationDTO convertToDTO(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        dto.setId(reservation.getId());
        dto.setCustomerId(reservation.getCustomer().getId());
        dto.setTableId(reservation.getRestaurantTable().getId());
        dto.setReservationTime(reservation.getReservationTime());
        dto.setDurationMinutes(reservation.getDurationMinutes());
        dto.setStatus(reservation.getStatus());
        return dto;
    }

    private Reservation convertToEntity(ReservationDTO dto) {
        Reservation r = new Reservation();
        r.setReservationTime(dto.getReservationTime());
        r.setDurationMinutes(dto.getDurationMinutes());
        r.setStatus(dto.getStatus());
        return r;
    }
}
