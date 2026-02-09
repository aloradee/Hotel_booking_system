package com.hotelbooking.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO для оценки отеля.
 * @author Кирилл_Христич
 */
@Data
@Schema(description = "DTO для оценки отеля")
public class RateHotelRequestDto {

    @NotNull(message = "Оценка обязательна")
    @Min(value = 1, message = "Оценка должна быть не менее 1")
    @Max(value = 5, message = "Оценка должна быть не более 5")
    @Schema(description = "Оценка отеля (от 1 до 5)", example = "5")
    private Integer rating;
}
