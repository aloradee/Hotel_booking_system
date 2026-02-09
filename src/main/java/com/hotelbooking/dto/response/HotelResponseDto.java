package com.hotelbooking.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO для ответа с информацией об отеле.
 * @author Кирилл_Христич
 */
@Data
@Schema(description = "DTO для ответа с информацией об отеле")
public class HotelResponseDto {

    @Schema(description = "ID отеля", example = "1")
    private Long id;

    @Schema(description = "Название отеля", example = "Grand Hotel")
    private String name;

    @Schema(description = "Заголовок объявления", example = "Роскошный отель в центре города")
    private String title;

    @Schema(description = "Город расположения отеля", example = "Москва")
    private String city;

    @Schema(description = "Адрес отеля", example = "ул. Тверская, 1")
    private String address;

    @Schema(description = "Расстояние от центра города в км", example = "1.5")
    private Double distanceFromCityCenter;

    @Schema(description = "Рейтинг отеля (от 1 до 5)", example = "4.5")
    private BigDecimal rating;

    @Schema(description = "Количество поставленных оценок", example = "150")
    private Integer numberOfRatings;
}
