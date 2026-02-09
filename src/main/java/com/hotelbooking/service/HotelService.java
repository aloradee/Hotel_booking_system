package com.hotelbooking.service;

import com.hotelbooking.dto.request.HotelRequestDto;
import com.hotelbooking.dto.request.HotelSearchCriteria;
import com.hotelbooking.dto.response.HotelResponseDto;
import com.hotelbooking.dto.response.PaginatedResponse;
import org.springframework.data.domain.Pageable;

/**
 * Сервис для работы с отелями.
 * @author Кирилл_Христич
 */
public interface HotelService {

    /**
     * Создает новый отель.
     * @param hotelRequestDto данные отеля
     * @return информация о созданном отеле
     */
    HotelResponseDto createHotel(HotelRequestDto hotelRequestDto);

    /**
     * Получает отель по ID.
     * @param id ID отеля
     * @return информация об отеле
     */
    HotelResponseDto getHotelById(Long id);

    /**
     * Обновляет информацию об отеле.
     * @param id ID отеля
     * @param hotelRequestDto новые данные отеля
     * @return обновленная информация об отеле
     */
    HotelResponseDto updateHotel(Long id, HotelRequestDto hotelRequestDto);

    /**
     * Удаляет отель.
     * @param id ID отеля
     */
    void deleteHotel(Long id);

    /**
     * Получает все отели с пагинацией.
     * @param pageable параметры пагинации
     * @return пагинированный список отелей
     */
    PaginatedResponse<HotelResponseDto> getAllHotels(Pageable pageable);

    /**
     * Ищет отели по критериям.
     * @param criteria критерии поиска
     * @param pageable параметры пагинации
     * @return пагинированный список найденных отелей
     */
    PaginatedResponse<HotelResponseDto> searchHotels(HotelSearchCriteria criteria, Pageable pageable);

    /**
     * Оценивает отель.
     * @param hotelId ID отеля
     * @param rating оценка
     * @return обновленная информация об отеле
     */
    HotelResponseDto rateHotel(Long hotelId, Integer rating);
}
