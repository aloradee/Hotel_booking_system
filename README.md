# Hotel Booking System 

Система бронирования отелей с полным функционалом управления отелями, комнатами и бронированиями. Проект реализует микросервисную архитектуру с использованием современных технологий.

## Функциональность

### Основные возможности
- **Управление отелями** (только для администраторов)
    - Создание, просмотр, редактирование и удаление отелей
    - Поиск отелей по различным критериям
    - Система рейтинга отелей (1-5 звёзд)

- **Управление комнатами** (только для администраторов)
    - Создание, просмотр, редактирование и удаление комнат
    - Поиск доступных комнат по датам и другим параметрам
    - Проверка доступности комнат на выбранные даты

- **Бронирования**
    - Создание бронирований (для авторизованных пользователей)
    - Просмотр своих бронирований
    - Отмена бронирований
    - Просмотр всех бронирований (для администраторов)

- **Пользователи и безопасность**
    - Регистрация новых пользователей
    - Аутентификация через Basic Auth
    - Ролевая модель: ROLE_USER и ROLE_ADMIN
    - Защищённые эндпоинты с проверкой прав

- **Статистика и аналитика**
    - Отслеживание регистраций пользователей
    - Отслеживание созданных бронирований
    - Экспорт статистики в CSV
    - Админ панель для просмотра статистики

### Технические особенности
- **Пагинация** на всех списках
- **Валидация** входных данных
- **Глобальная обработка ошибок**
- **Полная документация API** (Swagger/OpenAPI)
- **Интеграция с Kafka** для событий
- **Хранение статистики в MongoDB**
- **Панель администратора** на Thymeleaf

## Технологический стек

### Backend
- **Java 17**
- **Spring Boot 3.x**
- **Spring Security** (Basic Auth)
- **Spring Data JPA** (PostgreSQL)
- **Spring Data MongoDB**
- **Spring Kafka**
- **MapStruct** для маппинга DTO
- **Lombok** для уменьшения boilerplate кода
- **OpenAPI 3** для документации

### Базы данных
- **PostgreSQL** - основная база данных
- **MongoDB** - для хранения статистики

### Инфраструктура
- **Apache Kafka** - обработка событий
- **Docker** - контейнеризация
- **Maven** - управление зависимостями

### Установка и запуск

1. **Клонировать репозиторий**
```bash
git clone <repository-url>
cd hotel-booking
Настроить базы данных
```

```
sql
-- PostgreSQL
CREATE DATABASE hotel_booking;
CREATE USER hotel_user WITH PASSWORD 'hotel_password';
GRANT ALL PRIVILEGES ON DATABASE hotel_booking TO hotel_user;
Настроить Kafka
```

```bash
# Создать топики
kafka-topics --create --topic user-registration-events --bootstrap-server localhost:9092
kafka-topics --create --topic booking-events --bootstrap-server localhost:9092
```

Настроить приложение
Создать application.properties с настройками:

```properties
# Базы данных
spring.datasource.url=jdbc:postgresql://localhost:5432/hotel_booking
spring.datasource.username=hotel_user
spring.datasource.password=hotel_password

# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/hotel_booking_stats

# Kafka
spring.kafka.bootstrap-servers=localhost:9092

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Порт сервера
server.port=8080
```

Собрать и запустить

```bash
mvn clean install
mvn spring-boot:run
```

Использование Docker
```bash
# Запустить инфраструктуру
docker-compose up -d
```

# Собрать и запустить приложение
```
mvn clean install
java -jar target/hotel-booking-1.0.0.jar
```