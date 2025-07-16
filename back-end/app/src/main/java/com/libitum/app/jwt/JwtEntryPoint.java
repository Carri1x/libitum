package com.libitum.app.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * Clase que maneja los errores de autenticación no autorizada.
 * Implementa AuthenticationEntryPoint para interceptar las solicitudes no autorizadas.
 * 
 * @author Álvaro Carrión
 * @version 1.0
 * @since 1.0
 * @apiNote Esta clase es utilizada para manejar los errores de autenticación no autorizada.
 * @category Seguridad
 * @subcategory JWT
 */
public class JwtEntryPoint implements AuthenticationEntryPoint {

    /**
     * En este método devuelve un error en el HTTPResponse cuando un usuario no ha sido auntenticado
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //Devolvemos la resupesta en cuanto al error de auntenticación con una no autorización por medio del httpResponse y un mensaje personalizado 
        //para que pueda utilizarse posteriormente.
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autorizado");
    }
}
