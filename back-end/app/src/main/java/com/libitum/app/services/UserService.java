package com.libitum.app.services;

import com.libitum.app.model.user.User;
import com.libitum.app.repositories.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

@NoArgsConstructor
public class UserService implements UserDetailsService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     *  Devuelve un usuario por el nombre de usuario dentro de User de SpringSecurity gracias a UserDetails puede implementarse de buena práctica.
     * @param username the username identifying the user whose data is required.
     * @return User from UserDetails of SpringSecurity
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Buscamos el usuario y recogemos su rol creando SimpleGrantedAuthority para luego pasarlo dentro de la clase User de UserDetails
        User user = userRepository.findByName(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName().toString());

        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                Collections.singleton(authority)
        );
    }

    public boolean existByName(String name){
        return userRepository.existByName(name);
    }

    public void save(User user){
        userRepository.save(user);
    }
}
