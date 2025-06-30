package com.libitum.app.controllers;

import com.libitum.app.security.JwtService;
import com.libitum.app.repository.IUserRepository;
import com.libitum.app.auth.LoginRequest;
import com.libitum.app.auth.LoginResponse;
import com.libitum.app.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    /**
     * Busca un usuario dentro de la base de datos para ejecutar la acción de login
     * 
     * @param LoginRequest 
     * @return ResponseEntity
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        //Buscamos si el usuario está en la base de datos (su email)
        UserDTO user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        //Si las contraseñas no coinciden se devuelve un mensaje de error al usuario
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Credenciales incorrectas");
        }
        //En caso de que vaya todo bien se devuelve el token al navegador con respuesta 200 (aceptada) y el email del usuario dentro del token
        String token = jwtService.generateToken(user.getEmail());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
