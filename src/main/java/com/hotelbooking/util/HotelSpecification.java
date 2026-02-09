package com.hotelbooking.util;

import com.hotelbooking.entity.Hotel;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Утилитный класс для создания спецификаций поиска отелей.
 * @author Кирилл_Христич
 */
public class HotelSpecification {

    /**
     * Создает спецификацию для поиска отелей по критериям.
     * @param id ID отеля
     * @param name название отеля
     * @param title заголовок объявления
     * @param city город
     * @param address адрес
     * @param minDistance минимальное расстояние от центра
     * @param maxDistance максимальное расстояние от центра
     * @param minRating минимальный рейтинг
     * @param maxRating максимальный рейтинг
     * @param minRatingsCount минимальное количество оценок
     * @param maxRatingsCount максимальное количество оценок
     * @return спецификация для поиска
     */
    public static Specification<Hotel> searchHotels(Long id, String name, String title, String city,
                                                    String address, Double minDistance, Double maxDistance,
                                                    BigDecimal minRating, BigDecimal maxRating,
                                                    Integer minRatingsCount, Integer maxRatingsCount) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (id != null) {
                predicates.add(cb.equal(root.get("id"), id));
            }

            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (title != null && !title.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }

            if (city != null && !city.isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("city")), city.toLowerCase()));
            }

            if (address != null && !address.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("address")), "%" + address.toLowerCase() + "%"));
            }

            if (minDistance != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("distanceFromCityCenter"), minDistance));
            }

            if (maxDistance != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("distanceFromCityCenter"), maxDistance));
            }

            if (minRating != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("rating"), minRating));
            }

            if (maxRating != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("rating"), maxRating));
            }

            if (minRatingsCount != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("numberOfRatings"), minRatingsCount));
            }

            if (maxRatingsCount != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("numberOfRatings"), maxRatingsCount));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
