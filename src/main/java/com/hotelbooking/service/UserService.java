package com.hotelbooking.service;

import com.hotelbooking.dto.request.UserRequestDto;
import com.hotelbooking.dto.response.UserResponseDto;

import java.util.List;

/**
 * Сервис для работы с пользователями.
 * @author Кирилл_Христич
 */
public interface UserService {

    /**
     * Создает нового пользователя.
     * @param userRequestDto данные пользователя
     * @return информация о созданном пользователе
     */
    UserResponseDto createUser(UserRequestDto userRequestDto);

    /**
     * Получает пользователя по ID.
     * @param id ID пользователя
     * @return информация о пользователе
     */
    UserResponseDto getUserById(Long id);

    /**
     * Обновляет информацию о пользователе.
     * @param id ID пользователя
     * @param userRequestDto новые данные пользователя
     * @param currentUsername имя текущего пользователя
     * @return обновленная информация о пользователе
     */
    UserResponseDto updateUser(Long id, UserRequestDto userRequestDto, String currentUsername);

    /**
     * Удаляет пользователя.
     * @param id ID пользователя
     * @param currentUsername имя текущего пользователя
     */
    void deleteUser(Long id, String currentUsername);

    /**
     * Получает всех пользователей.
     * @return список всех пользователей
     */
    List<UserResponseDto> getAllUsers();

    /**
     * Находит пользователя по имени пользователя.
     * @param username имя пользователя
     * @return информация о пользователе
     */
    UserResponseDto findByUsername(String username);

    /**
     * Проверяет существование пользователя по имени пользователя или email.
     * @param username имя пользователя
     * @param email email пользователя
     * @return true если пользователь существует
     */
    boolean existsByUsernameOrEmail(String username, String email);
}
