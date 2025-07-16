package com.libitum.app.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
@Component
public class JwtUtil {
    /*Estos values van a sincronizarse con el application.properties, pudiendo posteriormente asignar en application.properties el valor de estas variables como
     variables de entorno ya sea dentro del ordenador local o dentro del servidor donde se haya desplegado la aplicación*/

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int expiration;

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

    public boolean validateToken(String token, UserDetails userDetails){
        final String userName = extractUserName(token);
        return (userName.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token){
        return extractAllClaims(token).getExpiration();
    }

    public Claims extractAllClaims(String token){
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUserName(String token){
        return extractAllClaims(token).getSubject();
    }
}
