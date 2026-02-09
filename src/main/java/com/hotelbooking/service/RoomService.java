package com.hotelbooking.service;

import com.hotelbooking.dto.request.RoomRequestDto;
import com.hotelbooking.dto.request.RoomSearchCriteria;
import com.hotelbooking.dto.response.PaginatedResponse;
import com.hotelbooking.dto.response.RoomResponseDto;
import org.springframework.data.domain.Pageable;

/**
 * Сервис для работы с комнатами.
 * @author Кирилл_Христич
 */
public interface RoomService {

    /**
     * Создает новую комнату.
     * @param roomRequestDto данные комнаты
     * @return информация о созданной комнате
     */
    RoomResponseDto createRoom(RoomRequestDto roomRequestDto);

    /**
     * Получает комнату по ID.
     * @param id ID комнаты
     * @return информация о комнате
     */
    RoomResponseDto getRoomById(Long id);

    /**
     * Обновляет информацию о комнате.
     * @param id ID комнаты
     * @param roomRequestDto новые данные комнаты
     * @return обновленная информация о комнате
     */
    RoomResponseDto updateRoom(Long id, RoomRequestDto roomRequestDto);

    /**
     * Удаляет комнату.
     * @param id ID комнаты
     */
    void deleteRoom(Long id);

    /**
     * Получает все комнаты с пагинацией.
     * @param pageable параметры пагинации
     * @return пагинированный список комнат
     */
    PaginatedResponse<RoomResponseDto> getAllRooms(Pageable pageable);

    /**
     * Ищет доступные комнаты по критериям.
     * @param criteria критерии поиска
     * @param pageable параметры пагинации
     * @return пагинированный список найденных комнат
     */
    PaginatedResponse<RoomResponseDto> searchAvailableRooms(RoomSearchCriteria criteria, Pageable pageable);
}
