package com.hotelbooking.entity.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Сущность для хранения статистики в MongoDB.
 * @author Кирилл_Христич
 */
@Document(collection = "statistics_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsRecord {

    @Id
    private String id;

    @Indexed
    @Field("event_type")
    private String eventType;

    @Indexed
    @Field("user_id")
    private Long userId;

    @Indexed
    @Field("timestamp")
    private LocalDateTime timestamp;

    @Field("data")
    private Map<String, Object> data;
}
