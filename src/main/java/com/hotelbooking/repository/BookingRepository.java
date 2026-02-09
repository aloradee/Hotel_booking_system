package com.hotelbooking.repository;

import com.hotelbooking.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * Репозиторий для работы с бронированиями.
 * @author Кирилл_Христич
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Находит бронирования пользователя с пагинацией.
     * @param userId ID пользователя
     * @param pageable параметры пагинации
     * @return страница бронирований
     */
    Page<Booking> findByUserId(Long userId, Pageable pageable);

    /**
     * Проверяет наличие пересекающихся бронирований для комнаты.
     * @param roomId ID комнаты
     * @param checkIn дата заезда
     * @param checkOut дата выезда
     * @return true если есть пересекающиеся бронирования
     */
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
            "FROM Booking b WHERE b.room.id = :roomId " +
            "AND ((b.checkInDate < :checkOut AND b.checkOutDate > :checkIn))")
    boolean existsOverlappingBooking(@Param("roomId") Long roomId,
                                     @Param("checkIn") LocalDate checkIn,
                                     @Param("checkOut") LocalDate checkOut);
}
