package com.hotelbooking.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO критериев поиска комнат.
 */
@Data
@Schema(description = "Критерии поиска комнат")
public class RoomSearchCriteria {

    @Schema(description = "ID комнаты", example = "1")
    private Long id;

    @Schema(description = "Название комнаты (частичное совпадение)", example = "люкс")
    private String name;

    @Schema(description = "Минимальная цена", example = "1000.00")
    private BigDecimal minPrice;

    @Schema(description = "Максимальная цена", example = "10000.00")
    private BigDecimal maxPrice;

    @Schema(description = "Максимальное количество гостей", example = "2")
    private Integer maxGuests;

    @Schema(description = "ID отеля", example = "1")
    private Long hotelId;

    @Future(message = "Дата заезда должна быть в будущем")
    @Schema(description = "Дата заезда", example = "2024-12-01")
    private LocalDate checkInDate;

    @Future(message = "Дата выезда должна быть в будущем")
    @Schema(description = "Дата выезда", example = "2024-12-10")
    private LocalDate checkOutDate;
}
