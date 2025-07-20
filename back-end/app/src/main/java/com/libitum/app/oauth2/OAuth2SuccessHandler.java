package com.libitum.app.oauth2;

import com.libitum.app.jwt.JwtUtil;
import com.libitum.app.model.Role;
import com.libitum.app.model.enums.RoleList;
import com.libitum.app.model.user.User;
import com.libitum.app.repositories.RoleRepository;
import com.libitum.app.repositories.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.AddressStandardClaim;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //Recogemos al usuario de authentication
        OidcUser oidcUser =(OidcUser) authentication.getPrincipal();
        String email = oidcUser.getEmail();


        User user = userRepository.findByEmail(email).orElseGet(() -> {
            AddressStandardClaim address =  oidcUser.getAddress();
            String country = null;
            String locality = null;
            String postalCode = null;
            String region = null;
            if(address != null){
                country = address.getCountry();
                locality = address.getLocality();
                postalCode = address.getPostalCode();
                region = address.getRegion();
            }
            Role role = roleRepository.findByName(RoleList.ROLE_USER).orElseThrow(() -> new RuntimeException("Rol no encontrado err=OA2.SH"));
            User newUser = User.builder()
                    .name(oidcUser.getName())
                    .surname(oidcUser.getFamilyName())
                    .email(email)
                    .emailVerified(oidcUser.getEmailVerified())
                    .phoneNumber(oidcUser.getPhoneNumber())
                    .phoneNumberVerified(oidcUser.getPhoneNumberVerified())
                    .role(role)
                    .nickname(oidcUser.getNickName())
                    .avatarUrl(oidcUser.getPicture())
                    .country(country)
                    .locality(locality)
                    .postalCode(postalCode)
                    .region(region)
                    .build();
                //oidcUser.getEmailVerified() o oidcUser.getPhoneNumberVerified()
                //true si Google ha verificado el número
                //false si no ha sido verificado (o es sospechoso)
                //null si el dato no fue proporcionado

            return newUser;
        });

        //todo -> cambiar la logica, comprobar cuando existe en la base de datos el usuario por el email coger el oidcUser y añadir datos que no tenga nuestro usuario

        userRepository.save(user);
        // todo -> pasar el jwt al method y clase para seguir con el login de Google
    }
}
