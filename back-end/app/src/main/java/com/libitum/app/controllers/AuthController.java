package com.libitum.app.controllers;

import com.libitum.app.email.EmailToken;
import com.libitum.app.email.service.EmailService;
import com.libitum.app.model.user.LoginUserDto;
import com.libitum.app.model.user.RegisterUserDto;
import com.libitum.app.model.user.ResponseUserDto;
import com.libitum.app.model.user.User;
import com.libitum.app.services.AuthService;
import com.libitum.app.services.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Controlador para la autenticación y registro de usuarios.
 * Proporciona endpoints para iniciar sesión, registrarse y obtener información de usuarios.
 * 
 * @author Álvaro Carrión
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    //Esto solo es para pruebas
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    /**
     * Método que autentica al usuario y devuelve un token JWT
     * En caso de que el usuario no exista o la contraseña sea incorrecta, se devolverá un mensaje de error (IMPORTANTE 
     *    QUE EL MENSAJE DE ERROR NO SEA MUY ESPECÍFICO PARA NO DAR PISTAS A UN POSIBLE ATACANTE).
     * 
     * @param loginUserDto Datos del usuario a autenticar
     * @param bindingResult Resultado de la validación de los datos del usuario
     * @return ResponseEntity con el token JWT o un mensaje de error
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginUserDto loginUserDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(Map.of("message", "Revise sus credenciales, binding error")); //Quitar información de binding error.
        }
        try{
            String jwt = authService.authenticate(loginUserDto.getEmail(),loginUserDto.getPassword());
            return ResponseEntity.ok().body(Map.of("token", jwt, "success", true));
        }catch(Exception e){
            return ResponseEntity.badRequest().body(Map.of("message", "Revise sus credenciales exception: "+e)); //Quitar lo de exception y la excepción
        }
    }

    /**
     * Método que registra un nuevo usuario en la base de datos
     * @param registerUserDto Datos del usuario a registrar
     * @param bindingResult Resultado de la validación de los datos del usuario
     * @return ResponseEntity con el mensaje de éxito o error
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterUserDto registerUserDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(Map.of("message","Revise los campos"));
        }
        try{
            authService.registerUser(registerUserDto);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of("message", String.format("Hola %s, te has registrado correctamente. Mira tu correo electrónico para verificar que existe." ,registerUserDto.getName())));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "El correo ya existe"));
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> verifiyEmail(@RequestBody String token){
        User user;
        String message = "Error, no se ha podido verificar el correo. Inténtentelo de nuevo";
        try{
            user = authService.verifyUser(token);
            System.out.println("95");
            if(user.getEmailVerified()){
                return ResponseEntity
                        .status(HttpStatus.ACCEPTED)
                        .body(Map.of("message", "El correo ha sido verificado correctamente."));
            }
        } catch (RuntimeException ex){ //No ponemos la IllegalArgumentException porque ya es una RuntimeException con el mismo fin
            message = ex.getMessage();
            System.out.println("103");
        }
        System.out.println("Pasa por 105");
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", message));
    }

}
