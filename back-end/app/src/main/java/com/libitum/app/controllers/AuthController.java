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

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginUserDto loginUserDto, BindingResult bindingResult){
        System.out.println("Intentando logear al usuario");
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body("Revise sus credenciales");
        }
        try{
            String jwt = authService.authenticate(loginUserDto.getEmail(),loginUserDto.getPassword());
            return ResponseEntity.ok(jwt);
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Revise sus credenciales.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterUserDto registerUserDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body("Revise los campos");
        }
        try{
            authService.registrerUser(registerUserDto);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(String.format("Hola %s, te has registrado correctamente."
                            ,registerUserDto.getName()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
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
