package com.hotelbooking.service.statistics;

import com.hotelbooking.event.BookingEvent;
import com.hotelbooking.event.UserRegistrationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Консьюмер Kafka для обработки статистических событий.
 * @author Кирилл_Христич
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaStatisticsConsumer {

    private final StatisticsService statisticsService;

    /**
     * Обрабатывает события регистрации пользователей.
     * @param event событие регистрации пользователя
     */
    @KafkaListener(topics = "user-registration-events", groupId = "statistics-group")
    public void consumeUserRegistrationEvent(UserRegistrationEvent event) {
        try {
            log.info("Received user registration event: {}", event);
            statisticsService.saveUserRegistrationEvent(event);
        } catch (Exception e) {
            log.error("Error processing user registration event", e);
        }
    }

    /**
     * Обрабатывает события создания бронирований.
     * @param event событие создания бронирования
     */
    @KafkaListener(topics = "booking-events", groupId = "statistics-group")
    public void consumeBookingEvent(BookingEvent event) {
        try {
            log.info("Received booking event: {}", event);
            statisticsService.saveBookingEvent(event);
        } catch (Exception e) {
            log.error("Error processing booking event", e);
        }
    }
}
