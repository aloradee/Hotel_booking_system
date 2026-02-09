package com.hotelbooking.controller;

import com.hotelbooking.dto.request.RoomRequestDto;
import com.hotelbooking.dto.request.RoomSearchCriteria;
import com.hotelbooking.dto.response.PaginatedResponse;
import com.hotelbooking.dto.response.RoomResponseDto;
import com.hotelbooking.service.RoomService;
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
 * Контроллер для работы с комнатами.
 * @author Кирилл_Христич
 */
@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
@Tag(name = "Комнаты", description = "API для управления комнатами")
public class RoomController {

    private final RoomService roomService;

    /**
     * Создает новую комнату.
     * @param roomRequestDto данные комнаты
     * @return информация о созданной комнате
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Создать новую комнату", description = "Доступно только администраторам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Комната создана"),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "404", description = "Отель не найден")
    })
    public RoomResponseDto createRoom(@Valid @RequestBody RoomRequestDto roomRequestDto) {
        return roomService.createRoom(roomRequestDto);
    }

    /**
     * Получает комнату по ID.
     * @param id ID комнаты
     * @return информация о комнате
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получить комнату по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комната найдена"),
            @ApiResponse(responseCode = "404", description = "Комната не найдена")
    })
    public RoomResponseDto getRoom(
            @Parameter(description = "ID комнаты", required = true)
            @PathVariable Long id) {
        return roomService.getRoomById(id);
    }

    /**
     * Обновляет информацию о комнате.
     * @param id ID комнаты
     * @param roomRequestDto новые данные комнаты
     * @return обновленная информация о комнате
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновить комнату", description = "Доступно только администраторам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комната обновлена"),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "404", description = "Комната или отель не найдены")
    })
    public RoomResponseDto updateRoom(
            @Parameter(description = "ID комнаты", required = true)
            @PathVariable Long id,
            @Valid @RequestBody RoomRequestDto roomRequestDto) {
        return roomService.updateRoom(id, roomRequestDto);
    }

    /**
     * Удаляет комнату.
     * @param id ID комнаты
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удалить комнату", description = "Доступно только администраторам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Комната удалена"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "404", description = "Комната не найдена")
    })
    public void deleteRoom(
            @Parameter(description = "ID комнаты", required = true)
            @PathVariable Long id) {
        roomService.deleteRoom(id);
    }

    /**
     * Получает список комнат с пагинацией.
     * @param page номер страницы
     * @param size размер страницы
     * @param sortBy поле для сортировки
     * @param direction направление сортировки
     * @return пагинированный список комнат
     */
    @GetMapping
    @Operation(summary = "Получить список комнат с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список комнат получен")
    })
    public PaginatedResponse<RoomResponseDto> getAllRooms(
            @Parameter(description = "Номер страницы (начиная с 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Поле для сортировки", example = "id")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Направление сортировки", example = "ASC")
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return roomService.getAllRooms(pageable);
    }

    /**
     * Ищет доступные комнаты по критериям.
     * @param criteria критерии поиска
     * @param page номер страницы
     * @param size размер страницы
     * @return пагинированный список найденных комнат
     */
    @GetMapping("/search")
    @Operation(summary = "Поиск доступных комнат по критериям с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Результаты поиска получены")
    })
    public PaginatedResponse<RoomResponseDto> searchAvailableRooms(
            @Valid RoomSearchCriteria criteria,
            @Parameter(description = "Номер страницы (начиная с 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return roomService.searchAvailableRooms(criteria, pageable);
    }
}
