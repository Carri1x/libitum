package com.libitum.app.controllers;

import com.libitum.app.model.user.LoginUserDto;
import com.libitum.app.model.user.RegisterUserDto;
import com.libitum.app.model.user.ResponseUserDto;
import com.libitum.app.services.AuthService;
import com.libitum.app.services.UserService;
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
        System.out.println("Intentando logear al usuario");
        if(bindingResult.hasErrors()){
            System.out.println("Tiene binding errors-----------------------------------------------------------------------------------");
            return ResponseEntity.badRequest().body(Map.of("error", "Revise sus credenciales, binding error")); //Quitar información de binding error.
        }
        try{
            String jwt = authService.authenticate(loginUserDto.getEmail(),loginUserDto.getPassword());
            return ResponseEntity.ok().body(Map.of("token", jwt, "success", true));
        }catch(Exception e){
            System.out.println("HA DADO EXCEPCION -------------------------------------------------------------------------------------");
            return ResponseEntity.badRequest().body(Map.of("error", "Revise sus credenciales exception: "+e)); //Quitar lo de exception y la excepción
        }
    }

    /**
     * Método que registra un nuevo usuario en la base de datos
     * @param registerUserDto Datos del usuario a registrar
     * @param bindingResult Resultado de la validación de los datos del usuario
     * @return ResponseEntity con el mensaje de éxito o error
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterUserDto registerUserDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body("Revise los campos");
        }
        try{
            authService.registerUser(registerUserDto);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(String.format("Hola %s, te has registrado correctamente."
                            ,registerUserDto.getName()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    /**
     * Método que devuelve todos los usuarios de la base de datos
     * @return List<ResponseUserDto>
     */
    @GetMapping("/users")
    public List<ResponseUserDto> getAllUsers(){
        return userService.getAllUsers();
    }

    /**
     * Si se tiene un token válido saldría en Postman la palabra "Autenticado", en caso de que en Postman en el apartado
     * Authorization -> type -> Bearer Token -> (El token generado), no se inserte un token válido saldrá un 401
     * @return ResponseEntity
     */
    @GetMapping("/bad")
    public String bad(){
       return "hola esto debería ir si tienes un token para autenticarte";
    }
}
