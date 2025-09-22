package com.libitum.app.services;

import com.libitum.app.email.EmailToken;
import com.libitum.app.email.service.EmailService;
import com.libitum.app.jwt.JwtUtil;
import com.libitum.app.model.Role;
import com.libitum.app.model.enums.RoleList;
import com.libitum.app.model.user.RegisterUserDto;
import com.libitum.app.model.user.User;
import com.libitum.app.repositories.EmailTokenVerificationRepository;
import com.libitum.app.repositories.RoleRepository;
import com.libitum.app.repositories.UserRepository;
import com.libitum.app.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    private final UserRepository userRepository;
    @Autowired
    private final RoleRepository roleRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder; // @Bean previamente configurado en SecurityConfig
    @Autowired
    private final JwtUtil jwtUtil; // @Bean previamente configurado en JwtUtil
    @Autowired
    private final AuthenticationManagerBuilder authenticationManagerBuilder; // @Bean previamente configurado en SecurityConfig (SE LLAMA authenticationProvider())
    @Autowired
    private final EmailService emailService; //Servicio para enviar una verificación al usuario para verificar que el correo es válido y suyo.
    @Autowired
    private final EmailTokenVerificationRepository emailTokenVerificationRepository;

    public AuthService(UserService userService, UserRepository userRepository, RoleRepository rolRepository, PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil, AuthenticationManagerBuilder authenticationManagerBuilder,
                       EmailService emailService, EmailTokenVerificationRepository emailTokenVerificationRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.emailService = emailService;
        this.emailTokenVerificationRepository = emailTokenVerificationRepository;
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

//    /**
//     * Método que registra un nuevo usuario en la base de datos
//     *
//     * @param registerUserDto Datos SOLAMENTE NECESARIOS del usuario a registrar
//     * @throws IllegalArgumentException Si el nombre de usuario ya existe
//     */
//    public void registerUser(RegisterUserDto registerUserDto) {
//        //AQUI SE COMPRUEBA QUE EL EMAIL NO EXISTE O NO ESTÁ VERIFICADO
//        if (userService.existByEmail(registerUserDto.getEmail())) {
//            System.out.println("117 -> Entra aquí");
//            User u = userRepository.findByEmail(registerUserDto.getEmail()).get(); //No creo que de vacío porque si existe el email
//            if (u.getEmailVerified()!=null && u.getEmailVerified()){ //Si el email está verificado salta la excepción, pero si no está verificado que se intente la verificación
//                throw new IllegalArgumentException(String.format("Este correo: %s, ya existe", registerUserDto.getEmail()));
//            } else { //Aquí actualizamos datos ya que el email ha sido encontrado pero no verificado ASÍ EVITAMOS DUPLICAR DATOS
//                User userUpdated = UserMapper.updateDataFromRegisterUserDtoToUser(registerUserDto, u); //Aqúi ya están todos los datos actualizados
//                SecureRandom secureRandom = new SecureRandom();
//                String sixFiguresNumber = String.valueOf(secureRandom.nextInt(900000) + 100000); //Hacemos como en Google damos un número de 6 cifras como código
//                EmailToken emailToken = new EmailToken(sixFiguresNumber, userUpdated);
//                userRepository.save(userUpdated);
//                Optional<EmailToken> existingToken = emailTokenVerificationRepository.findByUserId(userUpdated.getId());
//                if(existingToken.isPresent()){ //Si el EmailToken ya existe pues le cambiamos el código (update)
//                    existingToken.get().setToken(emailToken.getToken());
//                    existingToken.get().setExpirationDate(LocalDateTime.now().plusHours(24)); //Y volvemos a resetar las horas para que pueda darse de alta
//                    emailTokenVerificationRepository.save(existingToken.get());
//                } else {
//                    emailTokenVerificationRepository.save(emailToken);
//                }
//                 //Guardamos el token ya que tienen 24h de expiración del token
//                emailService.sendVerificationEmail(registerUserDto.getEmail(), sixFiguresNumber);
//                return;
//            }
//        }
//        //Comprobar las credenciales necesarias aun así se hayan comprobado en el front-end
//        if(registerUserDto.getEmail().isBlank() || registerUserDto.getEmail().isEmpty() ||
//            registerUserDto.getPassword().isEmpty() || registerUserDto.getPassword().isBlank()){
//            throw new IllegalArgumentException("Deben insertarse las credenciales necesarias");
//        }
//        //PROCEDEMOS A GUARDAR EL USUARIO EN LA BASE DE DATOS
//        Role role = roleRepository.findByName(RoleList.ROLE_USER)
//                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
//
//        String passwordEncoded = passwordEncoder.encode(registerUserDto.getPassword());
//        User user = UserMapper.fromRegisterUserDto(registerUserDto, role, passwordEncoded);
//        userService.save(user);
//        //Generamos un número random y recogemos el email del usuario para proceder con la comprobación del usuario
//        SecureRandom secureRandom = new SecureRandom();
//        String sixFiguresNumber = String.valueOf(secureRandom.nextInt(900000) + 100000); //Hacemos como en Google damos un número de 6 cifras como código
//        EmailToken emailToken = new EmailToken(sixFiguresNumber, registerUserDto);
//        emailTokenVerificationRepository.save(emailToken); //Guardamos el token ya que tienen 24h de expiración del token
//
//        emailService.sendVerificationEmail(registerUserDto.getEmail(), sixFiguresNumber); //Utilizamos el servicio para mandar el correo de verificación para email del usuario
//    }

    /**
     * Registra un nuevo usuario en la base de datos o actualiza uno existente no verificado
     *
     * @param registerUserDto Datos del usuario a registrar
     * @throws IllegalArgumentException Si las credenciales están vacías o el email ya existe y está verificado
     */
    @Transactional
    public void registerUser(RegisterUserDto registerUserDto) {
        validateCredentials(registerUserDto);

        Optional<User> existingUser = userRepository.findByEmail(registerUserDto.getEmail());

        if (existingUser.isPresent()) {
            handleExistingUser(existingUser.get(), registerUserDto);
        } else {
            createNewUser(registerUserDto);
        }
    }

    /**
     * Esto valida que las credenciales básicas no estén vacías
     */
    private void validateCredentials(RegisterUserDto registerUserDto) {
        if (isBlankOrEmpty(registerUserDto.getEmail()) ||
                isBlankOrEmpty(registerUserDto.getPassword())) {
            throw new IllegalArgumentException("Deben insertarse las credenciales necesarias");
        }
    }

    /**
     * Verifica si una cadena está vacía o en blanco
     */
    private boolean isBlankOrEmpty(String value) {
        return value == null || value.isEmpty() || value.isBlank();
    }

    /**
     * Maneja el caso cuando ya existe un usuario con el email proporcionado
     */
    private void handleExistingUser(User existingUser, RegisterUserDto registerUserDto) {
        if (isEmailVerified(existingUser)) {
            throw new IllegalArgumentException(
                    String.format("Este correo: %s, ya existe", registerUserDto.getEmail())
            );
        }

        updateUnverifiedUser(existingUser, registerUserDto);
    }

    /**
     * Verifica si el email del usuario está verificado
     */
    private boolean isEmailVerified(User user) {
        return user.getEmailVerified() != null && user.getEmailVerified();
    }

    /**
     * Actualiza los datos de un usuario no verificado y envía nuevo token de verificación
     */
    private void updateUnverifiedUser(User existingUser, RegisterUserDto registerUserDto) {
        User updatedUser = UserMapper.updateDataFromRegisterUserDtoToUser(registerUserDto, existingUser);
        userRepository.save(updatedUser);

        String verificationCode = generateVerificationCode();
        saveOrUpdateEmailToken(updatedUser, verificationCode);
        //Enviamos correo
        sendVerificationEmailAsync(registerUserDto.getEmail(), verificationCode);
    }

    /**
     * Crea un nuevo usuario en la base de datos
     */
    private void createNewUser(RegisterUserDto registerUserDto) {
        Role userRole = getUserRole();
        String encodedPassword = passwordEncoder.encode(registerUserDto.getPassword());

        User newUser = UserMapper.fromRegisterUserDto(registerUserDto, userRole, encodedPassword);
        userService.save(newUser); // Guardamos y obtenemos la entidad persistida
        Optional<User> savedUser = userService.getUserByEmail(newUser.getEmail());

        if(savedUser.isPresent()) {
            String verificationCode = generateVerificationCode();
            EmailToken emailToken = new EmailToken(verificationCode, savedUser.get()); // Usamos el usuario guardado, no el DTO
            emailTokenVerificationRepository.save(emailToken);
            //Enviamos correo
            sendVerificationEmailAsync(registerUserDto.getEmail(), verificationCode);
        } else {
            throw new RuntimeException("Lo siento este usuario no existe, no se ha insertado bien en la base de datos");
        }
    }

    /**
     * Obtiene el rol de usuario por defecto
     */
    private Role getUserRole() {
        return roleRepository.findByName(RoleList.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
    }

    /**
     * Genera un código de verificación de 6 dígitos
     */
    private String generateVerificationCode() {
        SecureRandom secureRandom = new SecureRandom();
        return String.valueOf(secureRandom.nextInt(900000) + 100000);
    }

    /**
     * Guarda un nuevo token o actualiza uno existente
     */
    private void saveOrUpdateEmailToken(User user, String verificationCode) {
        Optional<EmailToken> existingToken = emailTokenVerificationRepository.findByUserId(user.getId());

        if (existingToken.isPresent()) {
            updateExistingToken(existingToken.get(), verificationCode);
        } else {
            createNewToken(user, verificationCode);
        }
    }

    /**
     * Actualiza un token existente con nuevo código y fecha de expiración
     */
    private void updateExistingToken(EmailToken token, String verificationCode) {
        token.setToken(verificationCode);
        token.setExpirationDate(LocalDateTime.now().plusHours(24));
        emailTokenVerificationRepository.save(token);
    }

    /**
     * Crea un nuevo token de verificación
     */
    private void createNewToken(User user, String verificationCode) {
        EmailToken emailToken = new EmailToken(verificationCode, user);
        emailTokenVerificationRepository.save(emailToken);
    }

    /**
     * Envía el email de verificación de forma asíncrona
     */
    @Async("taskExecutor")
    private void sendVerificationEmailAsync(String email, String verificationCode) {
        try {
            emailService.sendVerificationEmail(email, verificationCode);
        } catch (Exception e) {
            // Log el error pero no interrumpir el proceso de registro
            System.out.printf("Error enviando email de verificación a {}: {}", email, e.getMessage());
        }
    }

    public User verifyUser(String token){
        //Buscamos el token, en caso de que exista es porque ha recibido dicho correo de verificación el usuario
        EmailToken emailTokenDataBase = emailTokenVerificationRepository.findByToken(token).orElseThrow(() ->
                new IllegalArgumentException("El código no es válido"));
        //Esta parte es liosa, pero léelo así = Si la fecha de expiración está en el pasado comparada con la fecha actual, entonces el token está expirado
        if(emailTokenDataBase.getExpirationDate().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Código expirado");
        }

        User user = emailTokenDataBase.getUser();
        user.setEmailVerified(true);
        return userRepository.save(user);
    }

    public List<EmailToken> getAllTokens(){
        return emailTokenVerificationRepository.findAll();
    }
}
