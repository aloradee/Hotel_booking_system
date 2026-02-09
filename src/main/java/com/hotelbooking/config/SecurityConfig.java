package com.hotelbooking.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфигурация безопасности приложения.
 * @author Кирилл_Христич
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Конфигурирует цепочку фильтров безопасности.
     * @param http объект для конфигурации безопасности
     * @return сконфигурированная цепочка фильтров
     * @throws Exception если возникла ошибка конфигурации
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/register",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/admin/**"
                        ).permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/hotels", "/api/rooms").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/hotels/*", "/api/rooms/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/hotels/**", "/api/rooms/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/**", "/api/bookings/all").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/hotels/*/rate").authenticated()
                        .requestMatchers("/api/bookings/**").authenticated()

                        .requestMatchers("/api/**").authenticated()

                        .anyRequest().permitAll()
                )
                .httpBasic(httpBasic -> httpBasic.realmName("Hotel Booking API"))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    /**
     * Создает кодировщик паролей.
     * @return кодировщик BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
