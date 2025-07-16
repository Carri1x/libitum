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


    public boolean existByName(String name){
        return userRepository.existsByName(name);
    }

    public void save(User user){
        userRepository.save(user);
    }

    public List<ResponseUserDto> getAllUsers(){
        return userRepository.findAll().stream().map(UserMapper::fromUser).toList();
    }
}
