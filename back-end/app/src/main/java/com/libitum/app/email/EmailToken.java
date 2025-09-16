package com.libitum.app.email;

import com.libitum.app.model.Role;
import com.libitum.app.model.enums.RoleList;
import com.libitum.app.model.user.RegisterUserDto;
import com.libitum.app.model.user.User;
import com.libitum.app.util.UserMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "email-token")
public class EmailToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private LocalDateTime expirationDate;
    @OneToOne
    private User user;

    public EmailToken(String token, User user){
        this.token = token;
        this.user = user;
        this.expirationDate = LocalDateTime.now().plusHours(24);
    }

    public EmailToken(String token, RegisterUserDto registerUserDto){
        Role role = new Role();
        role.setName(RoleList.ROLE_USER);
        this.token = token;
        this.user = UserMapper.fromRegisterUserDto(registerUserDto, role, encodePassword(registerUserDto.getPassword()));
        this.expirationDate = LocalDateTime.now().plusHours(24);
    }

    private String encodePassword(String password){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}
