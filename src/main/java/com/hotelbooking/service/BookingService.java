package com.hotelbooking.service;

import com.hotelbooking.dto.request.BookingRequestDto;
import com.hotelbooking.dto.response.BookingResponseDto;
import com.hotelbooking.dto.response.PaginatedResponse;
import org.springframework.data.domain.Pageable;

/**
 * Сервис для работы с бронированиями.
 * @author Кирилл_Христич
 */
public interface BookingService {

    /**
     * Создает новое бронирование.
     * @param bookingRequestDto данные бронирования
     * @param username имя пользователя
     * @return информация о созданном бронировании
     */
    BookingResponseDto createBooking(BookingRequestDto bookingRequestDto, String username);

    /**
     * Получает бронирование по ID.
     * @param id ID бронирования
     * @param username имя пользователя
     * @return информация о бронировании
     */
    BookingResponseDto getBookingById(Long id, String username);

    /**
     * Отменяет бронирование.
     * @param id ID бронирования
     * @param username имя пользователя
     */
    void cancelBooking(Long id, String username);

    /**
     * Получает бронирования пользователя с пагинацией.
     * @param username имя пользователя
     * @param pageable параметры пагинации
     * @return пагинированный список бронирований
     */
    PaginatedResponse<BookingResponseDto> getUserBookings(String username, Pageable pageable);

    /**
     * Получает все бронирования для администратора.
     * @param pageable параметры пагинации
     * @return пагинированный список всех бронирований
     */
    PaginatedResponse<BookingResponseDto> getAllBookingsAdmin(Pageable pageable);
}
