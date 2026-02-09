package com.hotelbooking.controller;

import com.hotelbooking.dto.request.BookingRequestDto;
import com.hotelbooking.dto.response.BookingResponseDto;
import com.hotelbooking.dto.response.PaginatedResponse;
import com.hotelbooking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для работы с бронированиями.
 * @author Кирилл_Христич
 */
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Tag(name = "Бронирования", description = "API для управления бронированиями")
public class BookingController {

    private final BookingService bookingService;

    /**
     * Создает новое бронирование.
     * @param bookingRequestDto данные бронирования
     * @param userDetails данные текущего пользователя
     * @return информация о созданном бронировании
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать новое бронирование")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Бронирование создано"),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "404", description = "Комната или пользователь не найдены")
    })
    public BookingResponseDto createBooking(
            @Valid @RequestBody BookingRequestDto bookingRequestDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return bookingService.createBooking(bookingRequestDto, userDetails.getUsername());
    }

    /**
     * Получает бронирование по ID.
     * @param id ID бронирования
     * @param userDetails данные текущего пользователя
     * @return информация о бронировании
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получить бронирование по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Бронирование найдено"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "404", description = "Бронирование не найдено")
    })
    public BookingResponseDto getBooking(
            @Parameter(description = "ID бронирования", required = true)
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return bookingService.getBookingById(id, userDetails.getUsername());
    }

    /**
     * Отменяет бронирование.
     * @param id ID бронирования
     * @param userDetails данные текущего пользователя
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Отменить бронирование")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Бронирование отменено"),
            @ApiResponse(responseCode = "400", description = "Нельзя отменить прошедшее бронирование"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "404", description = "Бронирование не найдено")
    })
    public void cancelBooking(
            @Parameter(description = "ID бронирования", required = true)
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        bookingService.cancelBooking(id, userDetails.getUsername());
    }

    /**
     * Получает бронирования текущего пользователя.
     * @param userDetails данные текущего пользователя
     * @param page номер страницы
     * @param size размер страницы
     * @return пагинированный список бронирований
     */
    @GetMapping("/my")
    @Operation(summary = "Получить мои бронирования с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список бронирований получен"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация")
    })
    public PaginatedResponse<BookingResponseDto> getMyBookings(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "Номер страницы (начиная с 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return bookingService.getUserBookings(userDetails.getUsername(), pageable);
    }

    /**
     * Получает все бронирования (только для администраторов).
     * @param page номер страницы
     * @param size размер страницы
     * @return пагинированный список всех бронирований
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить все бронирования", description = "Доступно только администраторам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список всех бронирований получен"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public PaginatedResponse<BookingResponseDto> getAllBookings(
            @Parameter(description = "Номер страницы (начиная с 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return bookingService.getAllBookingsAdmin(pageable);
    }
}
