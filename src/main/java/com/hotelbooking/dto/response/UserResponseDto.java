package com.hotelbooking.dto.response;

import com.hotelbooking.entity.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO для ответа с информацией о пользователе.
 * @author Кирилл_Христич
 */
@Data
@Schema(description = "DTO для ответа с информацией о пользователе")
public class UserResponseDto {

    @Schema(description = "ID пользователя", example = "1")
    private Long id;

    @Schema(description = "Имя пользователя", example = "john_doe")
    private String username;

    @Schema(description = "Email", example = "john@example.com")
    private String email;

    @Schema(description = "Роль пользователя", example = "ROLE_USER")
    private Role role;
}
