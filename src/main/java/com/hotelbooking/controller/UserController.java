package com.hotelbooking.controller;

import com.hotelbooking.dto.request.UserRequestDto;
import com.hotelbooking.dto.response.UserResponseDto;
import com.hotelbooking.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для работы с пользователями.
 * @author Кирилл_Христич
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "API для управления пользователями")
public class UserController {

    private final UserService userService;

    /**
     * Получает пользователя по ID.
     * @param id ID пользователя
     * @param userDetails данные текущего пользователя
     * @return информация о пользователе
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public UserResponseDto getUser(
            @Parameter(description = "ID пользователя", required = true)
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return userService.getUserById(id);
    }

    /**
     * Обновляет информацию о пользователе.
     * @param id ID пользователя
     * @param userRequestDto новые данные пользователя
     * @param userDetails данные текущего пользователя
     * @return обновленная информация о пользователе
     */
    @PutMapping("/{id}")
    @Operation(summary = "Обновить пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь обновлен"),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public UserResponseDto updateUser(
            @Parameter(description = "ID пользователя", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDto userRequestDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return userService.updateUser(id, userRequestDto, userDetails.getUsername());
    }

    /**
     * Удаляет пользователя.
     * @param id ID пользователя
     * @param userDetails данные текущего пользователя
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь удален"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public void deleteUser(
            @Parameter(description = "ID пользователя", required = true)
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteUser(id, userDetails.getUsername());
    }

    /**
     * Получает всех пользователей.
     * @return список всех пользователей
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить всех пользователей", description = "Доступно только администраторам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пользователей получен"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Получает информацию о текущем пользователе.
     * @param userDetails данные текущего пользователя
     * @return информация о текущем пользователе
     */
    @GetMapping("/me")
    @Operation(summary = "Получить информацию о текущем пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация получена"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация")
    })
    public UserResponseDto getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.findByUsername(userDetails.getUsername());
    }
}
