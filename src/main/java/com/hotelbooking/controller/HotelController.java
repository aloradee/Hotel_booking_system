package com.hotelbooking.controller;

import com.hotelbooking.dto.request.HotelRequestDto;
import com.hotelbooking.dto.request.HotelSearchCriteria;
import com.hotelbooking.dto.request.RateHotelRequestDto;
import com.hotelbooking.dto.response.HotelResponseDto;
import com.hotelbooking.dto.response.PaginatedResponse;
import com.hotelbooking.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для работы с отелями.
 * @author Кирилл_Христич
 */
@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
@Tag(name = "Отели", description = "API для управления отелями")
public class HotelController {

    private final HotelService hotelService;

    /**
     * Создает новый отель.
     * @param hotelRequestDto данные отеля
     * @return информация о созданном отеле
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Создать новый отель", description = "Доступно только администраторам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Отель создан"),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public HotelResponseDto createHotel(@Valid @RequestBody HotelRequestDto hotelRequestDto) {
        return hotelService.createHotel(hotelRequestDto);
    }

    /**
     * Получает отель по ID.
     * @param id ID отеля
     * @return информация об отеле
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получить отель по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отель найден"),
            @ApiResponse(responseCode = "404", description = "Отель не найден")
    })
    public HotelResponseDto getHotel(
            @Parameter(description = "ID отеля", required = true)
            @PathVariable Long id) {
        return hotelService.getHotelById(id);
    }

    /**
     * Обновляет информацию об отеле.
     * @param id ID отеля
     * @param hotelRequestDto новые данные отеля
     * @return обновленная информация об отеле
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновить отель", description = "Доступно только администраторам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отель обновлен"),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "404", description = "Отель не найден")
    })
    public HotelResponseDto updateHotel(
            @Parameter(description = "ID отеля", required = true)
            @PathVariable Long id,
            @Valid @RequestBody HotelRequestDto hotelRequestDto) {
        return hotelService.updateHotel(id, hotelRequestDto);
    }

    /**
     * Удаляет отель по id.
     * @param id ID отеля
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удалить отель", description = "Доступно только администраторам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Отель удален"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "404", description = "Отель не найден")
    })
    public void deleteHotel(
            @Parameter(description = "ID отеля", required = true)
            @PathVariable Long id) {
        hotelService.deleteHotel(id);
    }

    /**
     * Получает список отелей с пагинацией.
     * @param page номер страницы
     * @param size размер страницы
     * @param sortBy поле для сортировки
     * @param direction направление сортировки
     * @return пагинированный список отелей
     */
    @GetMapping
    @Operation(summary = "Получить список отелей с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список отелей получен")
    })
    public PaginatedResponse<HotelResponseDto> getAllHotels(
            @Parameter(description = "Номер страницы (начиная с 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Поле для сортировки", example = "id")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Направление сортировки", example = "ASC")
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return hotelService.getAllHotels(pageable);
    }

    /**
     * Ищет отели по критериям.
     * @param criteria критерии поиска
     * @param page номер страницы
     * @param size размер страницы
     * @return пагинированный список найденных отелей
     */
    @GetMapping("/search")
    @Operation(summary = "Поиск отелей по критериям с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Результаты поиска получены")
    })
    public PaginatedResponse<HotelResponseDto> searchHotels(
            HotelSearchCriteria criteria,
            @Parameter(description = "Номер страницы (начиная с 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return hotelService.searchHotels(criteria, pageable);
    }

    /**
     * Оценивает отель.
     * @param id ID отеля
     * @param rateRequest данные оценки
     * @return обновленная информация об отеле
     */
    @PutMapping("/{id}/rate")
    @Operation(summary = "Оценить отель")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отель оценен"),
            @ApiResponse(responseCode = "400", description = "Неверная оценка"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "404", description = "Отель не найден")
    })
    public HotelResponseDto rateHotel(
            @Parameter(description = "ID отеля", required = true)
            @PathVariable Long id,
            @Valid @RequestBody RateHotelRequestDto rateRequest) {
        return hotelService.rateHotel(id, rateRequest.getRating());
    }
}
