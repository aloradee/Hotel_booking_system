package com.hotelbooking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность отеля.
 * @author Кирилл_Христич
 */
@Entity
@Table(name = "hotels", indexes = {
        @Index(name = "idx_hotel_city", columnList = "city"),
        @Index(name = "idx_hotel_rating", columnList = "rating")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "rooms")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 50)
    private String city;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(name = "distance_from_city_center", precision = 5, scale = 2)
    private Double distanceFromCityCenter;

    @Column(precision = 3, scale = 2)
    @Builder.Default
    private BigDecimal rating = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    @Column(name = "number_of_ratings")
    @Builder.Default
    private Integer numberOfRatings = 0;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Room> rooms = new ArrayList<>();

    @PrePersist
    @PreUpdate
    public void prePersist() {
        if (rating == null) {
            rating = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        if (numberOfRatings == null) {
            numberOfRatings = 0;
        }
    }
}
