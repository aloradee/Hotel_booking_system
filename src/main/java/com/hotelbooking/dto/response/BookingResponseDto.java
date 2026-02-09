package com.hotelbooking.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO для ответа с информацией о бронировании.
 * @author Кирилл_Христич
 */
@Data
@Schema(description = "DTO для ответа с информацией о бронировании")
public class BookingResponseDto {

    @Schema(description = "ID бронирования", example = "1")
    private Long id;

    @Schema(description = "Дата заезда", example = "2024-12-01")
    private LocalDate checkInDate;

    @Schema(description = "Дата выезда", example = "2024-12-10")
    private LocalDate checkOutDate;

    @Schema(description = "ID комнаты", example = "1")
    private Long roomId;

    @Schema(description = "Название комнаты", example = "Люкс с видом на море")
    private String roomName;

    @Schema(description = "ID отеля", example = "1")
    private Long hotelId;

    @Schema(description = "Название отеля", example = "Grand Hotel")
    private String hotelName;

    @Schema(description = "ID пользователя", example = "1")
    private Long userId;

    @Schema(description = "Имя пользователя", example = "john_doe")
    private String userName;
}
