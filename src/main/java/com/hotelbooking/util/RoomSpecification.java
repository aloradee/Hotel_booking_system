package com.hotelbooking.util;

import com.hotelbooking.entity.Booking;
import com.hotelbooking.entity.Room;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Утилитный класс для создания спецификаций поиска комнат.
 * @author Кирилл_Христич
 */
public class RoomSpecification {

    /**
     * Создает спецификацию для поиска комнат по критериям.
     * @param id ID комнаты
     * @param name название комнаты
     * @param minPrice минимальная цена
     * @param maxPrice максимальная цена
     * @param maxGuests максимальное количество гостей
     * @param hotelId ID отеля
     * @param checkInDate дата заезда
     * @param checkOutDate дата выезда
     * @return спецификация для поиска
     */
    public static Specification<Room> searchRooms(Long id, String name, BigDecimal minPrice,
                                                  BigDecimal maxPrice, Integer maxGuests,
                                                  Long hotelId, LocalDate checkInDate,
                                                  LocalDate checkOutDate) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (id != null) {
                predicates.add(cb.equal(root.get("id"), id));
            }

            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            if (maxGuests != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("maxGuests"), maxGuests));
            }

            if (hotelId != null) {
                predicates.add(cb.equal(root.get("hotel").get("id"), hotelId));
            }

            if (checkInDate != null && checkOutDate != null) {
                if (checkInDate.isBefore(checkOutDate)) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<Booking> bookingRoot = subquery.from(Booking.class);

                    Predicate roomMatch = cb.equal(bookingRoot.get("room").get("id"), root.get("id"));
                    Predicate dateOverlap = cb.and(
                            cb.lessThan(bookingRoot.get("checkInDate"), checkOutDate),
                            cb.greaterThan(bookingRoot.get("checkOutDate"), checkInDate)
                    );

                    subquery.select(bookingRoot.get("room").get("id"))
                            .where(cb.and(roomMatch, dateOverlap));

                    predicates.add(cb.not(cb.exists(subquery)));
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
