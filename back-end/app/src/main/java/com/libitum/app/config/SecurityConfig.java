package com.libitum.app.config;

import com.libitum.app.jwt.JwtAuthenticationFilter;
import com.libitum.app.jwt.JwtEntryPoint;
import com.libitum.app.oauth2.OAuth2SuccessHandler;
import com.libitum.app.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Clase de configuración de seguridad para la aplicación.
 * Configura los filtros de seguridad, autenticación y autorización.
 * 
 * @author Álvaro Carrión
 * @version 1.0
 * @since 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /**
     * Método para filtrar todas las rutas que no hayan sido autenticadas a través de un filtro jwt
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception
     */
    @Bean
    protected SecurityFilterChain filterChain (HttpSecurity http, OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService,
                                               OAuth2SuccessHandler oAuth2SuccessHandler) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/login","/auth/register")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler( //Aquí hacer una clase externa que haga estas cosas
                                oAuth2SuccessHandler))
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtEntryPoint()))
                .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    /**
     * Método que devuelve nuestra clase JwtAuthenticationFilter para filtrar las peticiones
     * @return JwtAuthenticationFilter
     */
    @Bean
    public JwtAuthenticationFilter jwtTokenFilter(){
        return new JwtAuthenticationFilter();
    }
    /**
     * Método que devuelve nuestra clase JwtEntryPoint para manejar las excepciones de autenticación
     * @return JwtEntryPoint
     */
    @Bean
    public JwtEntryPoint jwtEntryPoint(){
        return new JwtEntryPoint();
    }
    /**
     * Método que devuelve un PasswordEncoder para encriptar las contraseñas
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    /**
     * Método que devuelve un UserDetailsService para cargar los usuarios desde la base de datos.
     * Utiliza nuestra clase UserService para cargar los usuarios.
     * @return UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService(){
        return new UserService();
    }
    /**
     * Método que devuelve un AuthenticationProvider para autenticar los usuarios.
     * Utiliza el UserDetailsService y el PasswordEncoder para autenticar los usuarios.
     * 
     * Este método es necesario para que Spring Security pueda autenticar los usuarios.
     * Se terminara de configurar en el futuro para incluir más detalles de autenticación.
     * Y acabará construyendose un AuthenticationManager que se utilizará en el servicio de autenticación AuthService.
     * @return AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    /**
     * Método que devuelve un CorsConfigurationSource para permitir el acceso a la API desde diferentes orígenes.
     * Utiliza una configuración CORS para permitir todos los métodos y encabezados.
     * 
     * Este método es necesario para que la API pueda ser consumida desde diferentes dominios.
     * @return CorsConfigurationSource
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of("*")); // Para desarrollo
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of(
                "Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"
        ));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
