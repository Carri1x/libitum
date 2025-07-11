package com.libitum.app.jwt;

import com.libitum.app.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Este @RequiredArgsConstructor hace lo mismo que @Autowired  pero en este caso fomenta la inmutabilidad como cuando se hace iyectando el atributo por el constructor como previamente se hace de normal.
@RequiredArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private JwtUtil jwtUtil;

    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authoritationHeader = request.getHeader("Authoritation");

        String userName = null;
        String jwt = null;

        //Si el header del objeto Authoritation del request no es null y empieza por "Bearer " es porque el token existe en el request... Podemos operar con él
        if(authoritationHeader != null && authoritationHeader.startsWith("Bearer ")){
            //Primero sacamos la palabra clave que va siempre junto al token (Bearer ) 7 chars!!!
            jwt = authoritationHeader.substring(7);
            //Aquí usamos la clase nuestra que hemos creado para extraer el username del token
            userName = jwtUtil.extractUserName(jwt);
        }

        if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userService.loadUserByUsername(userName);

            if(jwtUtil.validateToken(jwt, userDetails)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            filterChain.doFilter(request, response);
        }
    }
}
