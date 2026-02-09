package com.hotelbooking.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO для ошибок API.
 * @author Кирилл_Христич
 */
@Data
@AllArgsConstructor
@Schema(description = "DTO для ошибок API")
public class ApiError {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Время возникновения ошибки", example = "2024-01-15 10:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP статус код", example = "404")
    private int status;

    @Schema(description = "Тип ошибки", example = "Not Found")
    private String error;

    @Schema(description = "Сообщение об ошибке", example = "Ресурс не найден")
    private String message;

    @Schema(description = "Путь запроса", example = "/api/hotels/999")
    private String path;
}
