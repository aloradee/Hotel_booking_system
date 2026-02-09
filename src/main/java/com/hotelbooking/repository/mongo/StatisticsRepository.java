package com.hotelbooking.repository.mongo;

import com.hotelbooking.entity.mongo.StatisticsRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий MongoDB для работы со статистикой.
 * @author Кирилл_Христич
 */
@Repository
public interface StatisticsRepository extends MongoRepository<StatisticsRecord, String> {
}
