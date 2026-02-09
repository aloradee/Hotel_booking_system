package com.hotelbooking.controller.admin;

import com.hotelbooking.dto.response.PaginatedResponse;
import com.hotelbooking.service.BookingService;
import com.hotelbooking.service.HotelService;
import com.hotelbooking.service.RoomService;
import com.hotelbooking.service.UserService;
import com.hotelbooking.service.statistics.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Base64;

/**
 * Контроллер для админ панели.
 * @author Кирилл_Христич
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final HotelService hotelService;
    private final RoomService roomService;
    private final UserService userService;
    private final BookingService bookingService;
    private final StatisticsService statisticsService;

    /**
     * Отображает главную страницу админ панели.
     * @param model модель для передачи данных в представление
     * @return имя шаблона
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "admin/dashboard";
    }

    /**
     * Отображает страницу с отелями.
     * @param page номер страницы
     * @param size размер страницы
     * @param model модель для передачи данных
     * @return имя шаблона
     */
    @GetMapping("/hotels")
    public String hotels(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        PaginatedResponse<?> hotels = hotelService.getAllHotels(pageable);

        model.addAttribute("hotels", hotels);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", hotels.getTotalPages());

        return "admin/hotels";
    }

    /**
     * Отображает страницу с комнатами.
     * @param page номер страницы
     * @param size размер страницы
     * @param model модель для передачи данных
     * @return имя шаблона
     */
    @GetMapping("/rooms")
    public String rooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        PaginatedResponse<?> rooms = roomService.getAllRooms(pageable);

        model.addAttribute("rooms", rooms);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", rooms.getTotalPages());

        return "admin/rooms";
    }

    /**
     * Отображает страницу с пользователями.
     * @param model модель для передачи данных
     * @return имя шаблона
     */
    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    /**
     * Отображает страницу с бронированиями.
     * @param page номер страницы
     * @param size размер страницы
     * @param model модель для передачи данных
     * @return имя шаблона
     */
    @GetMapping("/bookings")
    public String bookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("checkInDate").descending());
        PaginatedResponse<?> bookings = bookingService.getAllBookingsAdmin(pageable);

        model.addAttribute("bookings", bookings);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookings.getTotalPages());

        return "admin/bookings";
    }

    /**
     * Отображает страницу со статистикой.
     * @param model модель для передачи данных
     * @return имя шаблона
     */
    @GetMapping("/statistics")
    public String statistics(Model model) {
        byte[] csvData = statisticsService.exportStatisticsToCsv();
        String csvBase64 = Base64.getEncoder().encodeToString(csvData);

        model.addAttribute("csvData", csvBase64);
        model.addAttribute("statistics", statisticsService.getAllStatistics());

        return "admin/statistics";
    }

    /**
     * Перенаправляет на экспорт CSV.
     * @return редирект на эндпоинт экспорта
     */
    @GetMapping("/export-csv")
    public String exportCsv() {
        return "redirect:/api/admin/statistics/export";
    }
}
