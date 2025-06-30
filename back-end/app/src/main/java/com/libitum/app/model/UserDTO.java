package com.libitum.app.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class UserDTO  implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;
    
    @Column(nullable = false, unique = true)
    private String email;    

    @Column(nullable = true, length = 15)
    private String telefono;

    @Column(nullable = false)
    private String password;

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles = new HashSet<>();

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
    private Set<UserDTO> seguidores = new HashSet<>();

    //------------------------------MÉTODOS-----------------------------------------

    public Set<String> getRoles() {
        return roles;
    }

    public void insertRol(String nuevoRol) {
        this.roles.add(nuevoRol);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
            .collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return email;
    }

    /*
    IMPLEMENTAR DE USER DETAILS
    @Override public boolean isAccountNonExpired() { *IMPLEMENTAR LÓGICA PARA AUTOMATIZAR EL ESTADO DEL USUARIO Y SU CUENTA* }
    @Override public boolean isAccountNonLocked() { *IMPLEMENTAR LÓGICA PARA AUTOMATIZAR EL ESTADO DEL USUARIO Y SU CUENTA* }
    @Override public boolean isCredentialsNonExpired() { *IMPLEMENTAR LÓGICA PARA AUTOMATIZAR EL ESTADO DEL USUARIO Y SU CUENTA* }
    @Override public boolean isEnabled() { *IMPLEMENTAR LÓGICA PARA AUTOMATIZAR EL ESTADO DEL USUARIO Y SU CUENTA* } 
    */

}
