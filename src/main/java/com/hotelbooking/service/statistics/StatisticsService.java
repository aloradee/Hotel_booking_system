package com.hotelbooking.service.statistics;

import com.hotelbooking.entity.mongo.StatisticsRecord;
import com.hotelbooking.event.BookingEvent;
import com.hotelbooking.event.UserRegistrationEvent;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сервис для работы со статистикой.
 * @author Кирилл_Христич
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final com.hotelbooking.repository.mongo.StatisticsRepository statisticsRepository;

    private static final String USER_REGISTRATION_TOPIC = "user-registration-events";
    private static final String BOOKING_EVENTS_TOPIC = "booking-events";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Отправляет событие регистрации пользователя в Kafka.
     * @param event событие регистрации пользователя
     */
    public void sendUserRegistrationEvent(UserRegistrationEvent event) {
        try {
            kafkaTemplate.send(USER_REGISTRATION_TOPIC, event);
            log.info("User registration event sent: {}", event);
        } catch (Exception e) {
            log.error("Failed to send user registration event", e);
            throw new RuntimeException("Failed to send user registration event", e);
        }
    }

    /**
     * Отправляет событие бронирования в Kafka.
     * @param event событие бронирования
     */
    public void sendBookingEvent(BookingEvent event) {
        try {
            kafkaTemplate.send(BOOKING_EVENTS_TOPIC, event);
            log.info("Booking event sent: {}", event);
        } catch (Exception e) {
            log.error("Failed to send booking event", e);
            throw new RuntimeException("Failed to send booking event", e);
        }
    }

    /**
     * Сохраняет событие регистрации пользователя в MongoDB.
     * @param event событие регистрации пользователя
     */
    public void saveUserRegistrationEvent(UserRegistrationEvent event) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("username", event.getUsername());
            data.put("email", event.getEmail());
            data.put("role", event.getRole());

            StatisticsRecord record = StatisticsRecord.builder()
                    .eventType("USER_REGISTRATION")
                    .userId(event.getUserId())
                    .timestamp(event.getTimestamp())
                    .data(data)
                    .build();

            statisticsRepository.save(record);
            log.info("User registration event saved: {}", event.getUserId());
        } catch (Exception e) {
            log.error("Failed to save user registration event", e);
        }
    }

    /**
     * Сохраняет событие бронирования в MongoDB.
     * @param event событие бронирования
     */
    public void saveBookingEvent(BookingEvent event) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("bookingId", event.getBookingId());
            data.put("roomId", event.getRoomId());
            data.put("checkInDate", event.getCheckInDate().toString());
            data.put("checkOutDate", event.getCheckOutDate().toString());

            StatisticsRecord record = StatisticsRecord.builder()
                    .eventType("BOOKING_CREATED")
                    .userId(event.getUserId())
                    .timestamp(event.getTimestamp())
                    .data(data)
                    .build();

            statisticsRepository.save(record);
            log.info("Booking event saved: {}", event.getBookingId());
        } catch (Exception e) {
            log.error("Failed to save booking event", e);
        }
    }

    /**
     * Экспортирует статистику в CSV.
     * @return массив байтов CSV файла
     */
    public byte[] exportStatisticsToCsv() {
        List<StatisticsRecord> records = getAllStatistics();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
             CSVWriter csvWriter = new CSVWriter(writer)) {

            String[] headers = {
                    "ID события",
                    "Тип события",
                    "ID пользователя",
                    "Время события",
                    "Дополнительные данные"
            };
            csvWriter.writeNext(headers);

            for (StatisticsRecord record : records) {
                String eventType = record.getEventType();
                String eventName = eventType.equals("USER_REGISTRATION") ? "Регистрация пользователя" : "Создание бронирования";

                String additionalData = "";
                if ("USER_REGISTRATION".equals(eventType)) {
                    Map<String, Object> data = record.getData();
                    additionalData = String.format("Имя: %s, Email: %s, Роль: %s",
                            data.get("username"), data.get("email"), data.get("role"));
                } else if ("BOOKING_CREATED".equals(eventType)) {
                    Map<String, Object> data = record.getData();
                    additionalData = String.format("ID бронирования: %s, ID комнаты: %s, Заезд: %s, Выезд: %s",
                            data.get("bookingId"), data.get("roomId"),
                            data.get("checkInDate"), data.get("checkOutDate"));
                }

                String[] row = {
                        record.getId(),
                        eventName,
                        record.getUserId().toString(),
                        DATE_FORMATTER.format(record.getTimestamp()),
                        additionalData
                };
                csvWriter.writeNext(row);
            }

            csvWriter.flush();
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Ошибка при экспорте статистики в CSV", e);
            throw new RuntimeException("Не удалось экспортировать статистику", e);
        }
    }

    /**
     * Получает всю статистику.
     * @return список всех записей статистики
     */
    public List<StatisticsRecord> getAllStatistics() {
        return statisticsRepository.findAll();
    }
}
