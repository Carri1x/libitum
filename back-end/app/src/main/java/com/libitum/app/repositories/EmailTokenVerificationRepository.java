package com.libitum.app.repositories;

import com.libitum.app.email.EmailToken;
import com.libitum.app.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailTokenVerificationRepository extends JpaRepository<EmailToken, Long> {
    Optional<EmailToken> findByToken(String token);
    Optional<EmailToken> findByUserId(String id);
}
