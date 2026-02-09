package com.hotelbooking.mapper;

import com.hotelbooking.dto.request.HotelRequestDto;
import com.hotelbooking.dto.response.HotelResponseDto;
import com.hotelbooking.entity.Hotel;
import org.mapstruct.*;

/**
 * Маппер для преобразования между Hotel и DTO.
 * @author Кирилл_Христич
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface HotelMapper {

    /**
     * Преобразует HotelRequestDto в Hotel.
     * @param dto DTO для создания/обновления отеля
     * @return сущность Hotel
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rating", constant = "0.00")
    @Mapping(target = "numberOfRatings", constant = "0")
    @Mapping(target = "rooms", ignore = true)
    Hotel toEntity(HotelRequestDto dto);

    /**
     * Преобразует Hotel в HotelResponseDto.
     * @param hotel сущность Hotel
     * @return DTO с информацией об отеле
     */
    HotelResponseDto toResponseDto(Hotel hotel);

    /**
     * Обновляет сущность Hotel из DTO.
     * @param dto DTO с новыми данными
     * @param hotel сущность для обновления
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "numberOfRatings", ignore = true)
    @Mapping(target = "rooms", ignore = true)
    void updateEntity(HotelRequestDto dto, @MappingTarget Hotel hotel);
}
