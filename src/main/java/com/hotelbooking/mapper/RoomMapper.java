package com.hotelbooking.mapper;

import com.hotelbooking.dto.request.RoomRequestDto;
import com.hotelbooking.dto.response.RoomResponseDto;
import com.hotelbooking.entity.Room;
import org.mapstruct.*;

/**
 * Маппер для преобразования между Room и DTO.
 * @author Кирилл_Христич
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {HotelMapper.class})
public interface RoomMapper {

    /**
     * Преобразует RoomRequestDto в Room.
     * @param dto DTO для создания/обновления комнаты
     * @return сущность Room
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hotel", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    Room toEntity(RoomRequestDto dto);

    /**
     * Преобразует Room в RoomResponseDto.
     * @param room сущность Room
     * @return DTO с информацией о комнате
     */
    @Mapping(source = "hotel.id", target = "hotelId")
    @Mapping(source = "hotel.name", target = "hotelName")
    RoomResponseDto toResponseDto(Room room);

    /**
     * Обновляет сущность Room из DTO.
     * @param dto DTO с новыми данными
     * @param room сущность для обновления
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hotel", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    void updateEntity(RoomRequestDto dto, @MappingTarget Room room);
}
