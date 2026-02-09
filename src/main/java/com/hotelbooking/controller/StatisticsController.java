package com.hotelbooking.controller;

import com.hotelbooking.service.statistics.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для работы со статистикой.
 * @author Кирилл_Христич
 */
@RestController
@RequestMapping("/admin/statistics")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Статистика", description = "API для работы со статистикой (только для администраторов)")
public class StatisticsController {

    private final StatisticsService statisticsService;

    /**
     * Экспортирует статистику в CSV файл.
     * @return CSV файл со статистикой
     */
    @GetMapping("/export")
    @Operation(summary = "Экспорт статистики в CSV", description = "Выгружает статистику в формате CSV файла")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CSV файл сгенерирован"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public ResponseEntity<byte[]> exportStatistics() {
        byte[] csvData = statisticsService.exportStatisticsToCsv();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "statistics.csv");
        headers.setContentLength(csvData.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(csvData);
    }
}
