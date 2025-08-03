package com.libitum.app.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * DTO para el registro de un nuevo usuario.
 * Contiene los campos necesarios para crear una cuenta de usuario.
 * Incluye validaciones para asegurar que los datos sean correctos.
 * 
 * @author Álvaro Carrión
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class RegisterUserDto {
    @NotBlank(message = "El nombre es obligatorio")
    private String name;
    private String surname; //Opcional
    @Email(message = "El email debe ser válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
    private String nickname;    //Opcional
    private String phoneNumber; //Opcional
}
