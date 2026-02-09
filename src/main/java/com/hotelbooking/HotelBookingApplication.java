package com.hotelbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения.
 * @author Кирилл_Христич
 */
@SpringBootApplication
public class HotelBookingApplication {

    /**
     * Точка входа в приложение.
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(HotelBookingApplication.class, args);
    }
}
