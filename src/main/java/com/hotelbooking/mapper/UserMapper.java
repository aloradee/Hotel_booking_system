package com.hotelbooking.mapper;

import com.hotelbooking.dto.request.UserRequestDto;
import com.hotelbooking.dto.response.UserResponseDto;
import com.hotelbooking.entity.User;
import org.mapstruct.*;

/**
 * Маппер для преобразования между User и DTO.
 * @author Кирилл_Христич
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    /**
     * Преобразует UserRequestDto в User.
     * @param dto DTO для создания/обновления пользователя
     * @return сущность User
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(UserRequestDto dto);

    /**
     * Преобразует User в UserResponseDto.
     * @param user сущность User
     * @return DTO с информацией о пользователе
     */
    @Mapping(target = "password", ignore = true)
    UserResponseDto toResponseDto(User user);

    /**
     * Обновляет сущность User из DTO.
     * @param dto DTO с новыми данными
     * @param user сущность для обновления
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateEntity(UserRequestDto dto, @MappingTarget User user);
}
