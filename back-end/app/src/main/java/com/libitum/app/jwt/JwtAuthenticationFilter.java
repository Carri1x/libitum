package com.libitum.app.jwt;

import com.libitum.app.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de autenticación JWT que se ejecuta en cada petición.
 * Comprueba si el token JWT es válido y autentica al usuario.
 * 
 * @author Álvaro Carrión
 * @version 1.0
 * @since 1.0
 * @apiNote Este filtro es utilizado para validar los tokens JWT en las peticiones a la API.
 * @category Seguridad
 */

//Este @RequiredArgsConstructor hace lo mismo que @Autowired  pero en este caso fomenta la inmutabilidad como cuando se hace iyectando el atributo por el constructor como previamente se hace de normal.
//@RequiredArgsConstructor Tiene que ser final la variable como -> private final AuthService authService;
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;

    /**
     * Este método se ejecuta en cada petición y comprueba si el token JWT es válido.
     * Si es válido, se autentica al usuario y se establece en el contexto de seguridad.
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param filterChain FilterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String userName = null;
        String jwt = null;

        //Si el header del objeto Authoritation del request no es null y empieza por "Bearer " es porque el token existe en el request... Podemos operar con él
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            //Primero sacamos la palabra clave que va siempre junto al token (Bearer ) 7 chars!!!
            jwt = authorizationHeader.substring(7);
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
        }
        filterChain.doFilter(request, response);
    }
}
