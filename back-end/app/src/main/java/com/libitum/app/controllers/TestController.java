package com.libitum.app.controllers;

import com.libitum.app.email.EmailToken;
import com.libitum.app.email.service.EmailService;
import com.libitum.app.model.user.ResponseUserDto;
import com.libitum.app.model.user.User;
import com.libitum.app.repositories.EmailTokenVerificationRepository;
import com.libitum.app.repositories.RoleRepository;
import com.libitum.app.repositories.UserRepository;
import com.libitum.app.services.AuthService;
import com.libitum.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private EmailTokenVerificationRepository emailTokenVerificationRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private AuthService authService;

    /**
     * Método que devuelve todos los usuarios de la base de datos
     * @return List<ResponseUserDto>
     */
    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
    @PostMapping("/remove/{id}")
    public String removeUserById(@PathVariable String id){
        try {
            Optional<EmailToken> emailToken = emailTokenVerificationRepository.findByUserId(id);
            if(emailToken.isPresent()){
                emailTokenVerificationRepository.deleteById(emailToken.get().getId());
            }
            userRepository.deleteById(id);
        }catch (Exception e){
            return e.getMessage();
        }
        return "ELIMINADO USUARIO: "+id;
    }

    @GetMapping("/tokens")
    public List<EmailToken> getAllTokens(){
        return authService.getAllTokens();
    }

    @GetMapping("/test")
    public String sendTestEmail(){
        emailService.sendVerificationEmail("acarrionr99@gmail.com", "123456");
        return "supuestamente funciona";
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
