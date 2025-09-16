package com.libitum.app.email;

import com.libitum.app.model.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private LocalDateTime expirationDate;

    @OneToOne
    private User user;

    public VerificationToken(String token, User user){
        this.token = token;
        this.user = user;
        this.expirationDate = LocalDateTime.now().plusHours(24);
    }

    public VerificationToken() {}
}
