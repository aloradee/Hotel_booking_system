package com.hotelbooking.exception;

/**
 * Исключение для случаев, когда ресурс не найден.
 * @author Кирилл_Христич
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
