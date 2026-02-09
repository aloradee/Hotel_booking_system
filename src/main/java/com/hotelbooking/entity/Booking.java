package com.hotelbooking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Сущность бронирования.
 * @author Кирилл_Христич
 */
@Entity
@Table(name = "bookings", indexes = {
        @Index(name = "idx_booking_user_id", columnList = "user_id"),
        @Index(name = "idx_booking_room_id", columnList = "room_id"),
        @Index(name = "idx_booking_dates", columnList = "check_in_date, check_out_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"room", "user"})
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false, foreignKey = @ForeignKey(name = "fk_booking_room"))
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_booking_user"))
    private User user;
}
