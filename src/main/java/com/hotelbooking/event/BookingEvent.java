package com.hotelbooking.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Событие создания бронирования.
 * @author Кирилл_Христич
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingEvent {
    private String eventType = "BOOKING_CREATED";
    private Long userId;
    private Long bookingId;
    private Long roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime timestamp;
}
