package com.hotelbooking.dto.request;

import com.hotelbooking.entity.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO для создания/обновления пользователя.
 * @author Кирилл_Христич
 */
@Data
@Schema(description = "DTO для создания/обновления пользователя")
public class UserRequestDto {

    @NotBlank(message = "Имя пользователя обязательно")
    @Size(min = 3, max = 50, message = "Имя пользователя должно быть от 3 до 50 символов")
    @Schema(description = "Имя пользователя", example = "john_doe")
    private String username;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
    @Schema(description = "Пароль", example = "password123")
    private String password;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Неверный формат email")
    @Schema(description = "Email", example = "john@example.com")
    private String email;

    @NotNull(message = "Роль обязательна")
    @Schema(description = "Роль пользователя", example = "ROLE_USER")
    private Role role;
}
