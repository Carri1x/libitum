package com.libitum.app.model.user;

import java.time.LocalDateTime;
import java.util.*;

import com.libitum.app.model.Role;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "usuarios")
@Data
@Builder
/*Al tener Builder de lombok podemos hacer esto 
User user = User.builder()
    .nombre("Álvaro")
    .apellidos("Carrión")
    .email("alvaro@example.com")
    .telefono("123456789")
    .password("hash")
    .build();
user.insertRol("USER"); */
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String surname;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;    

    @Column(nullable = true, length = 15)
    private String phoneNumber;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @OneToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    //Información pública
    @Column(nullable = true)
    private String nickname;
    @Column(nullable = true)
    private String description;
    @Column(nullable = true)
    private String avatarUrl;
    @Column(nullable = true)
    private String city;
    @Column(nullable = true)
    private String country;

    // Geo-ubicación actual
    @Column(nullable = true)
    private Double latitud;
    @Column(nullable = true)
    private Double longitud;

    // Actividad
    @Builder.Default
    private LocalDateTime registerDate = LocalDateTime.now();
    @Column(nullable = true)
    private LocalDateTime lastConnection;

    // Red social interna o followers
    @Builder.Default
    @ManyToMany
    @JoinTable(
        name = "user_followers",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    @Column(nullable = true)
    private Set<User> followers = new HashSet<>();

}
