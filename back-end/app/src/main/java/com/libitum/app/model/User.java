package com.libitum.app.model;

import java.time.LocalDateTime;
import java.util.*;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
UserDTO user = UserDTO.builder()
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
    private String nombre;

    @NotBlank
    @Column(nullable = false)
    private String apellidos;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;    

    @Column(nullable = true, length = 15)
    private String telefono;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @OneToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Rol rol;

    //Información pública
    private String nombreArtistico;
    private String descripcion;
    private String avatarUrl;
    private String ciudad;
    private String pais;

    // Geo-ubicación actual
    private Double latitud;
    private Double longitud;

    // Actividad
    @Builder.Default
    private LocalDateTime fechaRegistro = LocalDateTime.now();
    private LocalDateTime ultimaConexion;

    // Red social interna o followers
    @Builder.Default
    @ManyToMany
    @JoinTable(
        name = "user_followers",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private Set<User> seguidores = new HashSet<>();

}
