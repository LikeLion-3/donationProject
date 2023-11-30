package com.myapp.warmwave.domain.email.repository;

import com.myapp.warmwave.domain.email.entity.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {

    @Query("SELECT e FROM EmailAuth e WHERE e.email = :email AND e.authToken = :authToken AND e.expirationDate >= :currentTime AND e.expired = false")
    Optional<EmailAuth> findValidAuthByEmail(@Param("email") String email, @Param("authToken") String authToken, @Param("currentTime") LocalDateTime currentTime);

}
