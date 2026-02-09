package com.hotelbooking.service.impl;

import com.hotelbooking.dto.request.UserRequestDto;
import com.hotelbooking.dto.response.UserResponseDto;
import com.hotelbooking.entity.enums.Role;
import com.hotelbooking.entity.User;
import com.hotelbooking.event.UserRegistrationEvent;
import com.hotelbooking.exception.ResourceNotFoundException;
import com.hotelbooking.exception.ValidationException;
import com.hotelbooking.mapper.UserMapper;
import com.hotelbooking.repository.UserRepository;
import com.hotelbooking.service.UserService;
import com.hotelbooking.service.statistics.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с пользователями.
 * @author Кирилл_Христич
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final StatisticsService statisticsService;

    /**
     * Создает нового пользователя.
     * @param userRequestDto данные пользователя
     * @return информация о созданном пользователе
     */
    @Override
    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        log.info("Creating new user: {}", userRequestDto.getUsername());

        validateUserCreation(userRequestDto);

        User user = userMapper.toEntity(userRequestDto);
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        if (user.getRole() == null) {
            user.setRole(Role.ROLE_USER);
        }

        User savedUser = userRepository.save(user);
        log.info("User created with ID: {}", savedUser.getId());

        sendUserRegistrationEvent(savedUser);

        return userMapper.toResponseDto(savedUser);
    }

    /**
     * Получает пользователя по ID.
     * @param id ID пользователя
     * @return информация о пользователе
     */
    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        log.info("Getting user by ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с ID " + id + " не найден"));

        return userMapper.toResponseDto(user);
    }

    /**
     * Обновляет информацию о пользователе
     * @param id ID пользователя
     * @param userRequestDto новые данные пользователя
     * @param currentUsername имя текущего пользователя
     * @return обновленная информация о пользователе
     */
    @Override
    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto, String currentUsername) {
        log.info("Updating user with ID: {} by user: {}", id, currentUsername);

        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Текущий пользователь не найден"));

        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с ID " + id + " не найден"));

        if (!currentUser.getId().equals(id) && !currentUser.getRole().equals(Role.ROLE_ADMIN)) {
            throw new AccessDeniedException("Недостаточно прав для обновления пользователя");
        }

        if (!userToUpdate.getUsername().equals(userRequestDto.getUsername()) &&
                userRepository.existsByUsername(userRequestDto.getUsername())) {
            throw new ValidationException("Имя пользователя уже занято");
        }

        if (!userToUpdate.getEmail().equals(userRequestDto.getEmail()) &&
                userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new ValidationException("Email уже зарегистрирован");
        }

        if (!currentUser.getRole().equals(Role.ROLE_ADMIN)) {
            userRequestDto.setRole(userToUpdate.getRole());
        }

        userMapper.updateEntity(userRequestDto, userToUpdate);

        if (userRequestDto.getPassword() != null && !userRequestDto.getPassword().isEmpty()) {
            userToUpdate.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        }

        User updatedUser = userRepository.save(userToUpdate);
        log.info("User with ID {} updated", id);

        return userMapper.toResponseDto(updatedUser);
    }

    /**
     * Удаляет пользователя.
     * @param id ID пользователя
     * @param currentUsername имя текущего пользователя
     */
    @Override
    @Transactional
    public void deleteUser(Long id, String currentUsername) {
        log.info("Deleting user with ID: {} by user: {}", id, currentUsername);

        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Текущий пользователь не найден"));

        if (!currentUser.getId().equals(id) && !currentUser.getRole().equals(Role.ROLE_ADMIN)) {
            throw new AccessDeniedException("Недостаточно прав для удаления пользователя");
        }

        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Пользователь с ID " + id + " не найден");
        }

        userRepository.deleteById(id);
        log.info("User with ID {} deleted", id);
    }

    /**
     * Получает всех пользователей.
     * @return список всех пользователей
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        log.info("Getting all users");

        return userRepository.findAll().stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Находит пользователя по имени пользователя.
     * @param username имя пользователя
     * @return информация о пользователе
     */
    @Override
    @Transactional(readOnly = true)
    public UserResponseDto findByUsername(String username) {
        log.info("Finding user by username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с именем " + username + " не найден"));

        return userMapper.toResponseDto(user);
    }

    /**
     * Проверяет существование пользователя по имени пользователя или email.
     * @param username имя пользователя
     * @param email email пользователя
     * @return true если пользователь существует
     */
    @Override
    public boolean existsByUsernameOrEmail(String username, String email) {
        return userRepository.existsByUsernameOrEmail(username, email);
    }

    /**
     * Валидирует данные при создании пользователя.
     * @param userRequestDto данные пользователя
     */
    private void validateUserCreation(UserRequestDto userRequestDto) {
        if (existsByUsernameOrEmail(userRequestDto.getUsername(), userRequestDto.getEmail())) {
            throw new ValidationException("Пользователь с таким именем или email уже существует");
        }
    }

    /**
     * Отправляет событие о регистрации пользователя.
     * @param user созданный пользователь
     */
    private void sendUserRegistrationEvent(User user) {
        try {
            UserRegistrationEvent event = UserRegistrationEvent.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .role(user.getRole().name())
                    .timestamp(LocalDateTime.now())
                    .build();

            statisticsService.sendUserRegistrationEvent(event);
            log.info("User registration event sent for user ID: {}", user.getId());
        } catch (Exception e) {
            log.error("Failed to send user registration event for user ID: {}", user.getId(), e);
        }
    }
}
