package com.libitum.app.model.user;

import com.libitum.app.model.enums.RoleList;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public final class RegisterUserDto {
    @NotBlank(message = "El nombre es obligatorio")
    private String name;
    @NotBlank(message = "Los apellidos son obligatorios")
    private String surname;
    @Email(message = "El email debe ser válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
    private String nickname;    //Opcional
    private String phoneNumber; //Opcional
}
