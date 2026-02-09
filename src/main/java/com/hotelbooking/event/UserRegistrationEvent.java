package com.hotelbooking.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Событие регистрации пользователя.
 * @author Кирилл_Христич
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationEvent {
    private String eventType = "USER_REGISTRATION";
    private Long userId;
    private LocalDateTime timestamp;
    private String username;
    private String email;
    private String role;
}
