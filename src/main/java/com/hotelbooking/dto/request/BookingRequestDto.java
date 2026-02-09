package com.hotelbooking.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO для создания бронирования.
 * @author Кирилл_Христич
 */
@Data
@Schema(description = "DTO для создания бронирования")
public class BookingRequestDto {

    @NotNull(message = "Дата заезда обязательна")
    @Future(message = "Дата заезда должна быть в будущем")
    @Schema(description = "Дата заезда", example = "2024-12-01")
    private LocalDate checkInDate;

    @NotNull(message = "Дата выезда обязательна")
    @Future(message = "Дата выезда должна быть в будущем")
    @Schema(description = "Дата выезда", example = "2024-12-10")
    private LocalDate checkOutDate;

    @NotNull(message = "ID комнаты обязательно")
    @Schema(description = "ID комнаты", example = "1")
    private Long roomId;
}
