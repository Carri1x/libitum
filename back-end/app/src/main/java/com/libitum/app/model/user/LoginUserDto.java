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
