package com.hotelbooking.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * DTO для создания/обновления отеля.
 * @author Кирилл_Христич
 */
@Data
@Schema(description = "DTO для создания/обновления отеля")
public class HotelRequestDto {

    @NotBlank(message = "Название отеля обязательно")
    @Schema(description = "Название отеля", example = "Grand Hotel")
    private String name;

    @NotBlank(message = "Заголовок объявления обязателен")
    @Schema(description = "Заголовок объявления", example = "Роскошный отель в центре города")
    private String title;

    @NotBlank(message = "Город обязателен")
    @Schema(description = "Город расположения отеля", example = "Москва")
    private String city;

    @NotBlank(message = "Адрес обязателен")
    @Schema(description = "Адрес отеля", example = "ул. Тверская, 1")
    private String address;

    @NotNull(message = "Расстояние от центра города обязательно")
    @Positive(message = "Расстояние должно быть положительным числом")
    @Schema(description = "Расстояние от центра города в км", example = "1.5")
    private Double distanceFromCityCenter;
}
