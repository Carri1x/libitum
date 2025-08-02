package com.libitum.app.services;

import com.libitum.app.model.user.ResponseUserDto;
import com.libitum.app.model.user.User;
import com.libitum.app.repositories.UserRepository;
import com.libitum.app.util.UserMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Servicio de usuario que implementa UserDetailsService para proporcionar detalles del usuario.
 * Permite la carga de usuarios por nombre y proporciona métodos adicionales para gestionar usuarios.
 * 
 * @author Álvaro Carrión
 * @version 1.0
 * @since 1.0
 */
@Service
@NoArgsConstructor
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     *  Devuelve un usuario por el nombre de usuario dentro de User de SpringSecurity gracias a UserDetails puede implementarse de buena práctica.
     * @param email the username identifying the user whose data is required.
     * @return User from UserDetails of SpringSecurity
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //Buscamos el usuario y recogemos su rol creando SimpleGrantedAuthority para luego pasarlo dentro de la clase User de UserDetails
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User email not found"));
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName().toString());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(authority)
        );
    }

    /**
     * Method que busca un usuario por su nombre.
     * 
     * @param name Nombre del usuario a buscar
     * @return User encontrado o null si no existe
     */
    public boolean existByName(String name){
        return userRepository.existsByName(name);
    }
    /**
     * Method que busca un usuario por su email.
     * 
     * @param email Email del usuario a buscar
     * @return User encontrado o null si no existe
     */
    public boolean existByEmail(String email){
        return userRepository.existsByEmail(email);
    }
    /**
     * Method que obtiene todos los usuarios de la base de datos.
     * 
     * @return Lista de ResponseUserDto con todos los usuarios
     */
    public List<ResponseUserDto> getAllUsers(){
        return userRepository.findAll().stream().map(UserMapper::fromUser).toList();
    }

    /**
     * Method que guarda un nuevo usuario en la base de datos.
     * 
     * @param user Usuario a guardar
     */
    public void save(User user){
        userRepository.save(user);
    }
}
