package com.hotelbooking.controller;

import com.hotelbooking.dto.request.UserRequestDto;
import com.hotelbooking.dto.response.UserResponseDto;
import com.hotelbooking.entity.enums.Role;
import com.hotelbooking.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для работы с авторизацией.
 * @author Кирилл_Христич
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация", description = "API для регистрации и аутентификации")
public class AuthController {

    private final UserService userService;

    /**
     * Регистрирует нового пользователя.
     * @param userRequestDto данные пользователя
     * @param role роль пользователя
     * @return информация о созданном пользователе
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Регистрация нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Неверные данные или пользователь уже существует")
    })
    public UserResponseDto register(
            @Valid @RequestBody UserRequestDto userRequestDto,
            @Parameter(description = "Роль пользователя", example = "ROLE_USER")
            @RequestParam(required = false, defaultValue = "ROLE_USER") Role role) {

        userRequestDto.setRole(role);
        return userService.createUser(userRequestDto);
    }
}
