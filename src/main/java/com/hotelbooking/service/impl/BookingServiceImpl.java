package com.hotelbooking.service.impl;

import com.hotelbooking.dto.request.BookingRequestDto;
import com.hotelbooking.dto.response.BookingResponseDto;
import com.hotelbooking.dto.response.PaginatedResponse;
import com.hotelbooking.entity.Booking;
import com.hotelbooking.entity.Room;
import com.hotelbooking.entity.User;
import com.hotelbooking.event.BookingEvent;
import com.hotelbooking.exception.ResourceNotFoundException;
import com.hotelbooking.exception.ValidationException;
import com.hotelbooking.mapper.BookingMapper;
import com.hotelbooking.repository.BookingRepository;
import com.hotelbooking.repository.RoomRepository;
import com.hotelbooking.repository.UserRepository;
import com.hotelbooking.service.BookingService;
import com.hotelbooking.service.statistics.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Сервис для работы с бронированиями.
 * @author Кирилл_Христич
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;
    private final StatisticsService statisticsService;

    /**
     * Создает новое бронирование.
     * @param bookingRequestDto данные бронирования
     * @param username имя пользователя
     * @return информация о созданном бронировании
     */
    @Override
    @Transactional
    public BookingResponseDto createBooking(BookingRequestDto bookingRequestDto, String username) {
        log.info("Creating booking for user: {}", username);

        validateBookingDates(bookingRequestDto);

        Room room = roomRepository.findById(bookingRequestDto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Комната с ID " + bookingRequestDto.getRoomId() + " не найдена"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с именем " + username + " не найден"));

        checkRoomAvailability(room.getId(), bookingRequestDto.getCheckInDate(), bookingRequestDto.getCheckOutDate());

        Booking booking = bookingMapper.toEntity(bookingRequestDto);
        booking.setRoom(room);
        booking.setUser(user);

        Booking savedBooking = bookingRepository.save(booking);
        log.info("Booking created with ID: {} for user: {}", savedBooking.getId(), username);

        sendBookingEvent(savedBooking);

        return bookingMapper.toResponseDto(savedBooking);
    }

    /**
     * Получает бронирование по ID.
     * @param id ID бронирования
     * @param username имя пользователя
     * @return информация о бронировании
     */
    @Override
    @Transactional(readOnly = true)
    public BookingResponseDto getBookingById(Long id, String username) {
        log.info("Getting booking by ID: {} for user: {}", id, username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Бронирование с ID " + id + " не найдено"));

        if (!booking.getUser().getId().equals(user.getId()) &&
                !user.getRole().name().equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("Доступ запрещен");
        }

        return bookingMapper.toResponseDto(booking);
    }

    /**
     * Отменяет бронирование.
     * @param id ID бронирования
     * @param username имя пользователя
     */
    @Override
    @Transactional
    public void cancelBooking(Long id, String username) {
        log.info("Canceling booking with ID: {} for user: {}", id, username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Бронирование с ID " + id + " не найдено"));

        if (!booking.getUser().getId().equals(user.getId()) &&
                !user.getRole().name().equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("Доступ запрещен");
        }

        if (booking.getCheckInDate().isBefore(LocalDate.now())) {
            throw new ValidationException("Невозможно отменить прошедшее бронирование");
        }

        bookingRepository.delete(booking);
        log.info("Booking with ID {} canceled", id);
    }

    /**
     * Получает бронирования пользователя с пагинацией.
     * @param username имя пользователя
     * @param pageable параметры пагинации
     * @return пагинированный список бронирований
     */
    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<BookingResponseDto> getUserBookings(String username, Pageable pageable) {
        log.info("Getting bookings for user: {} with pagination: {}", username, pageable);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с именем " + username + " не найден"));

        Page<Booking> bookingPage = bookingRepository.findByUserId(user.getId(), pageable);
        return PaginatedResponse.of(bookingPage.map(bookingMapper::toResponseDto));
    }

    /**
     * Получает все бронирования для администратора.
     * @param pageable параметры пагинации
     * @return пагинированный список всех бронирований
     */
    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<BookingResponseDto> getAllBookingsAdmin(Pageable pageable) {
        log.info("Getting all bookings for admin with pagination: {}", pageable);

        Page<Booking> bookingPage = bookingRepository.findAll(pageable);
        return PaginatedResponse.of(bookingPage.map(bookingMapper::toResponseDto));
    }

    /**
     * Валидирует даты бронирования.
     * @param bookingRequestDto данные бронирования
     */
    private void validateBookingDates(BookingRequestDto bookingRequestDto) {
        if (bookingRequestDto.getCheckInDate().isAfter(bookingRequestDto.getCheckOutDate())) {
            throw new ValidationException("Дата заезда должна быть раньше даты выезда");
        }

        if (bookingRequestDto.getCheckInDate().isBefore(LocalDate.now())) {
            throw new ValidationException("Дата заезда должна быть в будущем");
        }

        if (bookingRequestDto.getCheckOutDate().isBefore(LocalDate.now())) {
            throw new ValidationException("Дата выезда должна быть в будущем");
        }
    }

    /**
     * Проверяет доступность комнаты на выбранные даты.
     * @param roomId ID комнаты
     * @param checkIn дата заезда
     * @param checkOut дата выезда
     */
    private void checkRoomAvailability(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        boolean isBooked = bookingRepository.existsOverlappingBooking(roomId, checkIn, checkOut);
        if (isBooked) {
            throw new ValidationException("Комната недоступна на выбранные даты");
        }
    }

    /**
     * Отправляет событие о создании бронирования.
     * @param booking созданное бронирование
     */
    private void sendBookingEvent(Booking booking) {
        try {
            BookingEvent event = BookingEvent.builder()
                    .userId(booking.getUser().getId())
                    .bookingId(booking.getId())
                    .roomId(booking.getRoom().getId())
                    .checkInDate(booking.getCheckInDate())
                    .checkOutDate(booking.getCheckOutDate())
                    .timestamp(LocalDateTime.now())
                    .build();

            statisticsService.sendBookingEvent(event);
            log.info("Booking event sent for booking ID: {}", booking.getId());
        } catch (Exception e) {
            log.error("Failed to send booking event for booking ID: {}", booking.getId(), e);
        }
    }
}
