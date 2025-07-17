package com.libitum.app.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la respuesta de un usuario.
 * Contiene información básica del usuario que se envía al cliente.
 * Evita que se envíen datos innecesarios y sensibles al cliente.
 * Este DTO es utilizado para mostrar la información del usuario en las vistas o APIs.
 * 
 * @author Álvaro Carrión
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class ResponseUserDto {
    private String id;
    private String name;
    private String surname;
    private String nickname;
    private String description;
    private String avatarUrl;
    private String city;
    private String country;
}
