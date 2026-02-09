package com.hotelbooking.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * DTO для пагинированного ответа.
 * @author Кирилл_Христич
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для пагинированного ответа")
public class PaginatedResponse<T> {

    @Schema(description = "Список элементов на текущей странице")
    private List<T> content;

    @Schema(description = "Номер текущей страницы (начинается с 0)", example = "0")
    private int pageNumber;

    @Schema(description = "Размер страницы", example = "10")
    private int pageSize;

    @Schema(description = "Общее количество элементов", example = "100")
    @JsonProperty("totalElements")
    private long totalElements;

    @Schema(description = "Общее количество страниц", example = "10")
    private int totalPages;

    @Schema(description = "Является ли текущая страница последней", example = "false")
    private boolean last;

    @Schema(description = "Является ли текущая страница первой", example = "true")
    private boolean first;

    @Schema(description = "Количество элементов на текущей странице", example = "10")
    private int numberOfElements;

    /**
     * Создает PaginatedResponse из объекта Page.
     * @param page объект Page
     * @param <T> тип элементов
     * @return PaginatedResponse
     */
    public static <T> PaginatedResponse<T> of(Page<T> page) {
        return new PaginatedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast(),
                page.isFirst(),
                page.getNumberOfElements()
        );
    }
}
