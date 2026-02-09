package com.hotelbooking.repository;

import com.hotelbooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с пользователями.
 * @author Кирилл_Христич
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Находит пользователя по имени пользователя.
     * @param username имя пользователя
     * @return Optional с пользователем
     */
    Optional<User> findByUsername(String username);

    /**
     * Проверяет существование пользователя по имени пользователя.
     * @param username имя пользователя
     * @return true если пользователь существует
     */
    boolean existsByUsername(String username);

    /**
     * Проверяет существование пользователя по email.
     * @param email email пользователя
     * @return true если пользователь существует
     */
    boolean existsByEmail(String email);

    /**
     * Проверяет существование пользователя по имени пользователя или email.
     * @param username имя пользователя
     * @param email email пользователя
     * @return true если пользователь существует
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
            "FROM User u WHERE u.username = :username OR u.email = :email")
    boolean existsByUsernameOrEmail(@Param("username") String username,
                                    @Param("email") String email);
}
