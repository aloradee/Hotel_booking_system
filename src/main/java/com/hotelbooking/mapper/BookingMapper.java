package com.hotelbooking.mapper;

import com.hotelbooking.dto.request.BookingRequestDto;
import com.hotelbooking.dto.response.BookingResponseDto;
import com.hotelbooking.entity.Booking;
import org.mapstruct.*;

/**
 * Маппер для преобразования между Booking и DTO.
 * @author Кирилл_Христич
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {RoomMapper.class, UserMapper.class})
public interface BookingMapper {

    /**
     * Преобразует BookingRequestDto в Booking.
     * @param dto DTO для создания бронирования
     * @return сущность Booking
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "user", ignore = true)
    Booking toEntity(BookingRequestDto dto);

    /**
     * Преобразует Booking в BookingResponseDto.
     * @param booking сущность Booking
     * @return DTO с информацией о бронировании
     */
    @Mapping(source = "room.id", target = "roomId")
    @Mapping(source = "room.name", target = "roomName")
    @Mapping(source = "room.hotel.id", target = "hotelId")
    @Mapping(source = "room.hotel.name", target = "hotelName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "userName")
    BookingResponseDto toResponseDto(Booking booking);
}
