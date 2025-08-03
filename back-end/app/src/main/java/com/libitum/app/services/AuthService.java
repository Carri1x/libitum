package com.libitum.app.services;

import com.libitum.app.jwt.JwtUtil;
import com.libitum.app.model.Role;
import com.libitum.app.model.enums.RoleList;
import com.libitum.app.model.user.RegisterUserDto;
import com.libitum.app.model.user.User;
import com.libitum.app.repositories.RoleRepository;
import com.libitum.app.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio de autenticación que maneja el registro y autenticación de usuarios.
 * Utiliza JwtUtil para generar tokens JWT y UserService para gestionar usuarios.
 * 
 * @author Álvaro Carrión
 * @version 1.0
 * @since 1.0
 */
@Service
public class AuthService {
    @Autowired
    private final UserService userService;
    @Autowired
    private final RoleRepository roleRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder; // @Bean previamente configurado en SecurityConfig
    @Autowired
    private final JwtUtil jwtUtil; // @Bean previamente configurado en JwtUtil
    @Autowired
    private final AuthenticationManagerBuilder authenticationManagerBuilder; // @Bean previamente configurado en SecurityConfig (SE LLAMA authenticationProvider())

    public AuthService(UserService userService, RoleRepository rolRepository, PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.userService = userService;
        this.roleRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    /**
     * Método que autentica al usuario y genera un token JWT
     * 
     * 
     * @param userName Nombre de usuario
     * @param password Contraseña del usuario
     * @return Token JWT
     */
    public String authenticate(String userName, String password) {
        // UsernamePasswordAuthenticationToken:
        // Crea un objeto con tus credenciales (username, password).
        // Spring Security lo usa para validar al usuario en el siguiente paso.
        // Aún no está autenticado, solo es una petición de autenticación.
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, password);
        // Este
        // authenticationManagerBuilder.getObject().authenticate(authenticationToken)
        // escoge los @Beans para autenticar
        // con los métodos authenticationProvider() de mi clase SecurityConfig para que
        // pueda autenticar al usuario y sus credenciales
        // que ya le hemos dicho previamente como hacerlo. HACE ALGO IMPLÍCITO.
        // Si todo es correcto: te devuelve un Authentication lleno de datos del
        // usuario.
        Authentication authResult = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // Guarda al usuario autenticado en el contexto de seguridad actual de Spring.
        // Spring usa el SecurityContextHolder para saber "quién eres" durante la
        // petición actual.
        // Así, en cualquier parte del código (filtros, controladores, servicios...),
        // puedes hacer:
        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // String currentUsername = auth.getName(); // o auth.getPrincipal()...
        // “Spring dice: Este usuario ya ha sido verificado. Guárdalo como el usuario
        // activo en esta petición.”
        SecurityContextHolder.getContext().setAuthentication(authResult);

        // Una vez hecho, registrado también el usuario en la petición genera el token y
        // le devuelve.
        return jwtUtil.generateToken(authResult);

    }

    /**
     * Método que registra un nuevo usuario en la base de datos
     * 
     * @param registerUserDto Datos SOLAMENTE NECESARIOS del usuario a registrar
     * @throws IllegalArgumentException Si el nombre de usuario ya existe
     */
    public void registerUser(RegisterUserDto registerUserDto) {
        if (userService.existByEmail(registerUserDto.getEmail())) {
            throw new IllegalArgumentException(String.format("Este correo: %s, ya existe", registerUserDto.getEmail()));
        }
        //Comprobar las credenciales necesarias aun así se hayan comprobado en el front-end
        if(registerUserDto.getEmail().isBlank() || registerUserDto.getEmail().isEmpty() ||
            registerUserDto.getPassword().isEmpty() || registerUserDto.getPassword().isBlank()){
            throw new IllegalArgumentException("Deben insertarse las credenciales necesarias");
        }
        Role role = roleRepository.findByName(RoleList.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        String passwordEncoded = passwordEncoder.encode(registerUserDto.getPassword());
        User user = UserMapper.fromRegisterUserDto(registerUserDto, role, passwordEncoded);
        userService.save(user);
    }

}
