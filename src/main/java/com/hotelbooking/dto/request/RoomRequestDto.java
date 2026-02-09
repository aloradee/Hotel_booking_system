package com.hotelbooking.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO для создания/обновления комнаты.
 * @author Кирилл_Христич
 */
@Data
@Schema(description = "DTO для создания/обновления комнаты")
public class RoomRequestDto {

    @NotBlank(message = "Название комнаты обязательно")
    @Schema(description = "Название комнаты", example = "Люкс с видом на море")
    private String name;

    @Schema(description = "Описание комнаты", example = "Просторный номер с панорамным видом")
    private String description;

    @NotBlank(message = "Номер комнаты обязателен")
    @Schema(description = "Номер комнаты", example = "101")
    private String number;

    @NotNull(message = "Цена обязательна")
    @Positive(message = "Цена должна быть положительной")
    @Schema(description = "Цена за ночь", example = "5000.00")
    private BigDecimal price;

    @NotNull(message = "Максимальное количество гостей обязательно")
    @Min(value = 1, message = "Минимум 1 гость")
    @Schema(description = "Максимальное количество гостей", example = "2")
    private Integer maxGuests;

    @NotNull(message = "ID отеля обязательно")
    @Schema(description = "ID отеля", example = "1")
    private Long hotelId;
}
