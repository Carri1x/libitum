package com.libitum.app.services;

import com.libitum.app.jwt.JwtUtil;
import com.libitum.app.model.Role;
import com.libitum.app.model.enums.RoleList;
import com.libitum.app.model.user.RegisterUserDto;
import com.libitum.app.model.user.User;
import com.libitum.app.repositories.RolRepository;
import com.libitum.app.util.UserMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public String authenticate(String userName, String password){
        /*UsernamePasswordAuthenticationToken.
        *Crea un objeto con tus credenciales (username, password).
        * Spring Security lo usa para validar al usuario en el siguiente paso.
        * Aún no está autenticado, solo es una petición de autenticación.*/
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, password);
        /*Este authenticationManagerBuilder.getObject().authenticate(authenticationToken) escoge los @Beans para autenticar con los métodos
        * authenticationProvider() de mi clase SecurityConfig para que pueda autenticar al usuario y sus credenciales que ya le hemos dicho previamente como hacerlo. HACE ALGO IMPLÍCITO.
        * Si todo es correcto: te devuelve un Authentication lleno de datos del usuario*/
        Authentication authResult = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        /*Guarda al usuario autenticado en el contexto de seguridad actual de Spring.

        Spring usa el SecurityContextHolder para saber "quién eres" durante la petición actual.

        Así, en cualquier parte del código (filtros, controladores, servicios...), puedes hacer:

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName(); // o auth.getPrincipal()...
        “Spring dice: Este usuario ya ha sido verificado. Guárdalo como el usuario activo en esta petición.”*/
        SecurityContextHolder.getContext().setAuthentication(authResult);
        //Una vez hecho, registrado también el usuario en la petición genera el token y le devuelve.
        return jwtUtil.generateToken(authResult);
    }

    public void registrerUser(RegisterUserDto registrerUserDto){
        if(userService.existByName(registrerUserDto.getName())){
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }
        Role role = Role.builder().name(RoleList.ROLE_USER).build();
        rolRepository.findRolByName(role.getName()).orElseThrow(()-> new RuntimeException("Rol no encontrado"));

        User user = UserMapper.fromRegisterUserDto(registrerUserDto, role, passwordEncoder.encode(registrerUserDto.getPassword()));
        userService.save(user);
    }

}
