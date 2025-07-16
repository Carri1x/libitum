package com.libitum.app.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Clase que contiene métodos para generar y validar tokens JWT.
 * Utiliza la librería JJWT para manejar los tokens de forma segura.
 * 
 * @author Álvaro Carrión
 * @version 1.0
 * @since 1.0
 * @apiNote Esta clase es parte de la implementación de seguridad basada en JWT (JSON Web Tokens).
 * @category
 * 
 * Esta clase es utilizada para:
 * - Generar un token JWT a partir de la autenticación del usuario.
 * - Validar un token JWT y comprobar su integridad.
 * - Extraer información del token, como el nombre de usuario y la fecha de expiración.
 */
@Component
public class JwtUtil {
    /*Estos values van a sincronizarse con el application.properties, pudiendo posteriormente asignar en application.properties el valor de estas variables como
     variables de entorno ya sea dentro del ordenador local o dentro del servidor donde se haya desplegado la aplicación*/
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int expiration;

    /**
     * Este método genera un token JWT a partir de la autenticación del usuario.
     * 
     * @param authentication Authentication
     * @return String token JWT
     */
    public String generateToken (Authentication authentication){
        UserDetails mainUser = (UserDetails) authentication.getPrincipal();
        //Hay ahora muchas formas deprecated, aun así en stackOverflow he visto algunos ejemplos que ha hecho la gente y este es el que más me a convencido.
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder().subject(mainUser.getUsername())
                .issuedAt(new Date())
                .expiration(new Date( new Date().getTime() + expiration * 1000L))
                .signWith(key)
                .compact();
    }

    /**
     * Este método valida el token JWT comprobando si el nombre de usuario coincide y si el token no ha expirado.
     * 
     * @param token String token JWT
     * @param userDetails UserDetails
     * @return boolean true si es válido, false en caso contrario
     */
    public boolean validateToken(String token, UserDetails userDetails){
        final String userName = extractUserName(token);
        return (userName.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Este método comprueba si el token JWT ha expirado.
     * Se usa dentro del método validateToken para comprobar si el token es válido.
     * 
     * @param token String token JWT
     * @return boolean true si ha expirado, false en caso contrario
     */
    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    /**
     * Este método extrae todos los claims del token JWT.
     * Las Claims son los datos que contiene el token, como el nombre de usuario, la fecha de expiración, etc.
     * 
     * @param token String token JWT
     * @return Claims claims del token
     */
    public Claims extractAllClaims(String token){
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Este método extrae la fecha de expiración del token JWT.
     * 
     * @param token String token JWT
     * @return Date fecha de expiración
     */
    public Date extractExpiration(String token){
        return extractAllClaims(token).getExpiration();
    }

    
    /**
     * Este método extrae el nombre de usuario del token JWT.
     * 
     * @param token String token JWT
     * @return String nombre de usuario
     */
    public String extractUserName(String token){
        return extractAllClaims(token).getSubject();
    }
}
