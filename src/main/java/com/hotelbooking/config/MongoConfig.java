package com.hotelbooking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Конфигурация MongoDB для хранения статистики.
 * @author Кирилл_Христич
 */
@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "com.hotelbooking.repository.mongo")
public class MongoConfig {
}
