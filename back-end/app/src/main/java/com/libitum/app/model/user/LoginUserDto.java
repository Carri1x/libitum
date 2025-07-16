package com.libitum.app.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * DTO para el inicio de sesión del usuario.
 * Contiene los campos necesarios para autenticar al usuario.
 *
 * @author Álvaro Carrión
 * @version 1.0
 * @since 1.0
 * @apiNote Este DTO es utilizado para el inicio de sesión en las APIs.
 * @category Usuario
 * @subcategory Inicio de sesión
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDto {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
