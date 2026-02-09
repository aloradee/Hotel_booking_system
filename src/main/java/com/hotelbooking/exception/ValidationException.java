package com.hotelbooking.exception;

/**
 * Исключение для случаев ошибок валидации.
 * @author Кирилл_Христич
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
