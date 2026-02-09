package com.hotelbooking.service.impl;

import com.hotelbooking.dto.request.RoomRequestDto;
import com.hotelbooking.dto.request.RoomSearchCriteria;
import com.hotelbooking.dto.response.PaginatedResponse;
import com.hotelbooking.dto.response.RoomResponseDto;
import com.hotelbooking.entity.Hotel;
import com.hotelbooking.entity.Room;
import com.hotelbooking.exception.ResourceNotFoundException;
import com.hotelbooking.mapper.RoomMapper;
import com.hotelbooking.repository.HotelRepository;
import com.hotelbooking.repository.RoomRepository;
import com.hotelbooking.service.RoomService;
import com.hotelbooking.util.RoomSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис для работы с комнатами.
 * @author Кирилл_Христич
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomMapper roomMapper;

    /**
     * Создает новую комнату.
     * @param roomRequestDto данные комнаты
     * @return информация о созданной комнате
     */
    @Override
    @Transactional
    public RoomResponseDto createRoom(RoomRequestDto roomRequestDto) {
        log.info("Creating new room for hotel ID: {}", roomRequestDto.getHotelId());

        Hotel hotel = hotelRepository.findById(roomRequestDto.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Отель с ID " + roomRequestDto.getHotelId() + " не найден"));

        Room room = roomMapper.toEntity(roomRequestDto);
        room.setHotel(hotel);

        Room savedRoom = roomRepository.save(room);
        log.info("Room created with ID: {} for hotel ID: {}", savedRoom.getId(), hotel.getId());

        return roomMapper.toResponseDto(savedRoom);
    }

    /**
     * Получает комнату по ID.
     * @param id ID комнаты
     * @return информация о комнате
     */
    @Override
    @Transactional(readOnly = true)
    public RoomResponseDto getRoomById(Long id) {
        log.info("Getting room by ID: {}", id);

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Комната с ID " + id + " не найдена"));

        return roomMapper.toResponseDto(room);
    }

    /**
     * Обновляет информацию о комнате.
     * @param id ID комнаты
     * @param roomRequestDto новые данные комнаты
     * @return обновленная информация о комнате
     */
    @Override
    @Transactional
    public RoomResponseDto updateRoom(Long id, RoomRequestDto roomRequestDto) {
        log.info("Updating room with ID: {}", id);

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Комната с ID " + id + " не найдена"));

        if (!room.getHotel().getId().equals(roomRequestDto.getHotelId())) {
            Hotel hotel = hotelRepository.findById(roomRequestDto.getHotelId())
                    .orElseThrow(() -> new ResourceNotFoundException("Отель с ID " + roomRequestDto.getHotelId() + " не найден"));
            room.setHotel(hotel);
        }

        roomMapper.updateEntity(roomRequestDto, room);
        Room updatedRoom = roomRepository.save(room);
        log.info("Room with ID {} updated", id);

        return roomMapper.toResponseDto(updatedRoom);
    }

    /**
     * Удаляет комнату.
     * @param id ID комнаты
     */
    @Override
    @Transactional
    public void deleteRoom(Long id) {
        log.info("Deleting room with ID: {}", id);

        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Комната с ID " + id + " не найдена");
        }

        roomRepository.deleteById(id);
        log.info("Room with ID {} deleted", id);
    }

    /**
     * Получает все комнаты с пагинацией.
     * @param pageable параметры пагинации
     * @return пагинированный список комнат
     */
    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<RoomResponseDto> getAllRooms(Pageable pageable) {
        log.info("Getting all rooms with pagination: {}", pageable);

        Page<Room> roomPage = roomRepository.findAll(pageable);
        return PaginatedResponse.of(roomPage.map(roomMapper::toResponseDto));
    }

    /**
     * Ищет доступные комнаты по критериям.
     * @param criteria критерии поиска
     * @param pageable параметры пагинации
     * @return пагинированный список найденных комнат
     */
    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<RoomResponseDto> searchAvailableRooms(RoomSearchCriteria criteria, Pageable pageable) {
        log.info("Searching available rooms with criteria: {}", criteria);

        Specification<Room> spec = RoomSpecification.searchRooms(
                criteria.getId(), criteria.getName(), criteria.getMinPrice(),
                criteria.getMaxPrice(), criteria.getMaxGuests(), criteria.getHotelId(),
                criteria.getCheckInDate(), criteria.getCheckOutDate()
        );

        Page<Room> roomPage = roomRepository.findAll(spec, pageable);
        return PaginatedResponse.of(roomPage.map(roomMapper::toResponseDto));
    }
}
