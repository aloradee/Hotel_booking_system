package com.hotelbooking.service.impl;

import com.hotelbooking.dto.request.HotelRequestDto;
import com.hotelbooking.dto.request.HotelSearchCriteria;
import com.hotelbooking.dto.response.HotelResponseDto;
import com.hotelbooking.dto.response.PaginatedResponse;
import com.hotelbooking.entity.Hotel;
import com.hotelbooking.exception.ResourceNotFoundException;
import com.hotelbooking.exception.ValidationException;
import com.hotelbooking.mapper.HotelMapper;
import com.hotelbooking.repository.HotelRepository;
import com.hotelbooking.service.HotelService;
import com.hotelbooking.util.HotelSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Сервис для работы с отелями.
 * @author Кирилл_Христич
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    /**
     * Создает новый отель.
     * @param hotelRequestDto данные отеля
     * @return информация о созданном отеле
     */
    @Override
    @Transactional
    public HotelResponseDto createHotel(HotelRequestDto hotelRequestDto) {
        log.info("Creating new hotel: {}", hotelRequestDto.getName());

        Hotel hotel = hotelMapper.toEntity(hotelRequestDto);
        hotel.setRating(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        hotel.setNumberOfRatings(0);

        Hotel savedHotel = hotelRepository.save(hotel);
        log.info("Hotel created with ID: {}", savedHotel.getId());

        return hotelMapper.toResponseDto(savedHotel);
    }

    /**
     * Получает отель по ID.
     * @param id ID отеля
     * @return информация об отеле
     */
    @Override
    @Transactional(readOnly = true)
    public HotelResponseDto getHotelById(Long id) {
        log.info("Getting hotel by ID: {}", id);

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Отель с ID " + id + " не найден"));

        return hotelMapper.toResponseDto(hotel);
    }

    /**
     * Обновляет информацию об отеле.
     * @param id ID отеля
     * @param hotelRequestDto новые данные отеля
     * @return обновленная информация об отеле
     */
    @Override
    @Transactional
    public HotelResponseDto updateHotel(Long id, HotelRequestDto hotelRequestDto) {
        log.info("Updating hotel with ID: {}", id);

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Отель с ID " + id + " не найден"));

        hotelMapper.updateEntity(hotelRequestDto, hotel);
        Hotel updatedHotel = hotelRepository.save(hotel);
        log.info("Hotel with ID {} updated", id);

        return hotelMapper.toResponseDto(updatedHotel);
    }

    /**
     * Удаляет отель.
     * @param id ID отеля
     */
    @Override
    @Transactional
    public void deleteHotel(Long id) {
        log.info("Deleting hotel with ID: {}", id);

        if (!hotelRepository.existsById(id)) {
            throw new ResourceNotFoundException("Отель с ID " + id + " не найден");
        }

        hotelRepository.deleteById(id);
        log.info("Hotel with ID {} deleted", id);
    }

    /**
     * Получает все отели с пагинацией.
     * @param pageable параметры пагинации
     * @return пагинированный список отелей
     */
    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<HotelResponseDto> getAllHotels(Pageable pageable) {
        log.info("Getting all hotels with pagination: {}", pageable);

        Page<Hotel> hotelPage = hotelRepository.findAll(pageable);
        return PaginatedResponse.of(hotelPage.map(hotelMapper::toResponseDto));
    }

    /**
     * Ищет отели по критериям.
     * @param criteria критерии поиска
     * @param pageable параметры пагинации
     * @return пагинированный список найденных отелей
     */
    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<HotelResponseDto> searchHotels(HotelSearchCriteria criteria, Pageable pageable) {
        log.info("Searching hotels with criteria: {}", criteria);

        Specification<Hotel> spec = HotelSpecification.searchHotels(
                criteria.getId(), criteria.getName(), criteria.getTitle(), criteria.getCity(),
                criteria.getAddress(), criteria.getMinDistance(), criteria.getMaxDistance(),
                criteria.getMinRating(), criteria.getMaxRating(),
                criteria.getMinRatingsCount(), criteria.getMaxRatingsCount()
        );

        Page<Hotel> hotelPage = hotelRepository.findAll(spec, pageable);
        return PaginatedResponse.of(hotelPage.map(hotelMapper::toResponseDto));
    }

    /**
     * Оценивает отель.
     * @param hotelId ID отеля
     * @param rating оценка
     * @return обновленная информация об отеле
     */
    @Override
    @Transactional
    public HotelResponseDto rateHotel(Long hotelId, Integer rating) {
        log.info("Rating hotel {} with rating: {}", hotelId, rating);

        if (rating < 1 || rating > 5) {
            throw new ValidationException("Оценка должна быть от 1 до 5");
        }

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Отель с ID " + hotelId + " не найден"));

        BigDecimal totalRating = hotel.getRating()
                .multiply(BigDecimal.valueOf(hotel.getNumberOfRatings()));

        totalRating = totalRating
                .subtract(hotel.getRating())
                .add(BigDecimal.valueOf(rating));

        int newNumberOfRatings = hotel.getNumberOfRatings() + 1;

        BigDecimal newRating = totalRating
                .divide(BigDecimal.valueOf(newNumberOfRatings), 1, RoundingMode.HALF_UP);

        hotel.setRating(newRating);
        hotel.setNumberOfRatings(newNumberOfRatings);

        Hotel updatedHotel = hotelRepository.save(hotel);
        log.info("Hotel {} rated. New rating: {}, total ratings: {}",
                hotelId, newRating, newNumberOfRatings);

        return hotelMapper.toResponseDto(updatedHotel);
    }
}
