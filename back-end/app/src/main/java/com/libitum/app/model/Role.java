package com.libitum.app.model;

import com.libitum.app.model.enums.RoleList;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa un rol de un usuario en la aplicación.
 * Utilizada para definir los permisos y accesos de los usuarios.
 * 
 * @author Álvaro Carrión
 * @version 1.0
 * @since 1.0
 * @apiNote Esta clase es utilizada para representar los roles de usuario en la aplicación.
 * @category Seguridad
 * @subcategory Modelo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private RoleList name;
}