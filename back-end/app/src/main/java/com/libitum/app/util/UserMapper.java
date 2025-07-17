package com.libitum.app.util;

import com.libitum.app.model.Role;
import com.libitum.app.model.user.RegisterUserDto;
import com.libitum.app.model.user.ResponseUserDto;
import com.libitum.app.model.user.User;

import java.time.LocalDateTime;

/**
 * Clase de utilidad para mapear entre diferentes objetos relacionados con el usuario.
 * Proporciona métodos para convertir DTOs a entidades y viceversa.
 * 
 * @author Álvaro Carrión
 * @version 1.0
 * @since 1.0
 */
public class UserMapper {
    /**
     * Método para crear la Entidad USER en el momento que se registra un usuario
     * ***SE CONTROLAN DATOS SENSIBLES EN LA CLASE RETORNADA User***
     *
     * @param userDto con los datos suministrados por el formulario de registro
     * @param rol que va a llevar a cabo en caso de que sea (ADMIN, USER, VIEWER)
     * @param passwordEncoded constraseña previamente codificada en algún punto de la aplicación como en UserService
     * @return User, para insertar en la base de datos Postgres
     */
    public static User fromRegisterUserDto(RegisterUserDto userDto, Role rol, String passwordEncoded){
        return User.builder()
                .name(userDto.getName())
                .surname(userDto.getSurname())
                .email(userDto.getEmail())
                .password(passwordEncoded)
                .nickname(userDto.getNickname())
                .phoneNumber(userDto.getPhoneNumber())
                .role(rol)
                .registerDate(LocalDateTime.now())
                .build();
    }

    /**
     * Método para crear un ResponseUserDto, así mismo hacer más seguro el transporte de los datos del usuario
     * llevando únicamente al front-end los datos que serán públicos para las personas.
     * *** NO SE CONTROLAN DATOS SENSIBLES EN LA CLASE RETORNADA ResponseUserDto***
     * Datos sensibles que se omiten (Contraseña, Email, UUID...)
     * @param user El usuario que vamos a querer convertir a ResponseUserDto
     * @return El objeto para transporte de datos no sensibles
     */
    public static ResponseUserDto fromUser(User user){
        return ResponseUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .nickname(user.getNickname())
                .description(user.getDescription())
                .avatarUrl(user.getAvatarUrl())
                .city(user.getCity())
                .country(user.getCountry())
                .build();
    }
}
