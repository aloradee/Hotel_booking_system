package com.hotelbooking.exception;

import com.hotelbooking.dto.response.ApiError;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Глобальный обработчик исключений.
 * @author Кирилл_Христич
 */
@RestControllerAdvice
@Slf4j
@Hidden
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключение ResourceNotFoundException.
     * @param ex исключение
     * @param request HTTP запрос
     * @return информация об ошибке
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {

        log.warn("Resource not found: {}", ex.getMessage());

        return new ApiError(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Resource Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    /**
     * Обрабатывает исключение ValidationException.
     * @param ex исключение
     * @param request HTTP запрос
     * @return информация об ошибке
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(
            ValidationException ex, HttpServletRequest request) {

        log.warn("Validation error: {}", ex.getMessage());

        return new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    /**
     * Обрабатывает исключение MethodArgumentNotValidException.
     * @param ex исключение
     * @return карта ошибок валидации
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        log.warn("Method argument validation error");

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }

    /**
     * Обрабатывает исключения аутентификации.
     * @param ex исключение
     * @param request HTTP запрос
     * @return информация об ошибке
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleAuthenticationException(
            AuthenticationException ex, HttpServletRequest request) {

        log.warn("Authentication error: {}", ex.getMessage());

        return new ApiError(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Authentication Failed",
                "Неверные учетные данные",
                request.getRequestURI()
        );
    }

    /**
     * Обрабатывает исключение BadCredentialsException.
     * @param ex исключение
     * @param request HTTP запрос
     * @return информация об ошибке
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleBadCredentialsException(
            BadCredentialsException ex, HttpServletRequest request) {

        log.warn("Bad credentials error");

        return new ApiError(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Bad Credentials",
                "Неверное имя пользователя или пароль",
                request.getRequestURI()
        );
    }

    /**
     * Обрабатывает исключение AccessDeniedException.
     * @param ex исключение
     * @param request HTTP запрос
     * @return информация об ошибке
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleAccessDeniedException(
            AccessDeniedException ex, HttpServletRequest request) {

        log.warn("Access denied: {}", ex.getMessage());

        return new ApiError(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Access Denied",
                "Недостаточно прав для выполнения операции",
                request.getRequestURI()
        );
    }

    /**
     * Обрабатывает исключение DataIntegrityViolationException.
     * @param ex исключение
     * @param request HTTP запрос
     * @return информация об ошибке
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, HttpServletRequest request) {

        log.error("Data integrity violation", ex);

        String message = "Нарушение целостности данных";
        if (ex.getMessage() != null && ex.getMessage().contains("duplicate key")) {
            message = "Запись с такими данными уже существует";
        }

        return new ApiError(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Data Integrity Violation",
                message,
                request.getRequestURI()
        );
    }

    /**
     * Обрабатывает исключение MethodArgumentTypeMismatchException.
     * @param ex исключение
     * @param request HTTP запрос
     * @return информация об ошибке
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        log.warn("Method argument type mismatch: {}", ex.getMessage());

        return new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Type Mismatch",
                "Неверный тип параметра: " + ex.getName(),
                request.getRequestURI()
        );
    }

    /**
     * Обрабатывает исключение MissingServletRequestParameterException.
     * @param ex исключение
     * @param request HTTP запрос
     * @return информация об ошибке
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex, HttpServletRequest request) {

        log.warn("Missing request parameter: {}", ex.getMessage());

        return new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Missing Parameter",
                "Отсутствует обязательный параметр: " + ex.getParameterName(),
                request.getRequestURI()
        );
    }

    /**
     * Обрабатывает исключение HttpMessageNotReadableException.
     * @param ex исключение
     * @param request HTTP запрос
     * @return информация об ошибке
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        log.warn("HTTP message not readable: {}", ex.getMessage());

        return new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Request Body",
                "Неверный формат тела запроса",
                request.getRequestURI()
        );
    }

    /**
     * Обрабатывает все непредвиденные исключения.
     * @param ex исключение
     * @param request HTTP запрос
     * @return информация об ошибке
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleGlobalException(
            Exception ex, HttpServletRequest request) {

        log.error("Unhandled exception", ex);

        return new ApiError(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Внутренняя ошибка сервера",
                request.getRequestURI()
        );
    }
}
