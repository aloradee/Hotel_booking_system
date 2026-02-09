package com.hotelbooking.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO для ответа с информацией о комнате.
 * @author Кирилл_Христич
 */
@Data
@Schema(description = "DTO для ответа с информацией о комнате")
public class RoomResponseDto {

    @Schema(description = "ID комнаты", example = "1")
    private Long id;

    @Schema(description = "Название комнаты", example = "Люкс с видом на море")
    private String name;

    @Schema(description = "Описание комнаты", example = "Просторный номер с панорамным видом")
    private String description;

    @Schema(description = "Номер комнаты", example = "101")
    private String number;

    @Schema(description = "Цена за ночь", example = "5000.00")
    private BigDecimal price;

    @Schema(description = "Максимальное количество гостей", example = "2")
    private Integer maxGuests;

    @Schema(description = "ID отеля", example = "1")
    private Long hotelId;

    @Schema(description = "Название отеля", example = "Grand Hotel")
    private String hotelName;
}
