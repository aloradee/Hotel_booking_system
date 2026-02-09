package com.hotelbooking.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO критериев поиска отелей.
 * @author Кирилл_Христич
 */
@Data
@Schema(description = "Критерии поиска отелей")
public class HotelSearchCriteria {

    @Schema(description = "ID отеля", example = "1")
    private Long id;

    @Schema(description = "Название отеля (частичное совпадение)", example = "Grand")
    private String name;

    @Schema(description = "Заголовок объявления (частичное совпадение)", example = "роскошный")
    private String title;

    @Schema(description = "Город", example = "Москва")
    private String city;

    @Schema(description = "Адрес (частичное совпадение)", example = "Тверская")
    private String address;

    @Schema(description = "Минимальное расстояние от центра", example = "0.5")
    private Double minDistance;

    @Schema(description = "Максимальное расстояние от центра", example = "5.0")
    private Double maxDistance;

    @Schema(description = "Минимальный рейтинг", example = "4.0")
    private BigDecimal minRating;

    @Schema(description = "Максимальный рейтинг", example = "5.0")
    private BigDecimal maxRating;

    @Schema(description = "Минимальное количество оценок", example = "10")
    private Integer minRatingsCount;

    @Schema(description = "Максимальное количество оценок", example = "1000")
    private Integer maxRatingsCount;
}
