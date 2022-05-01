package br.com.fit.apigateway.crud.controller;

import br.com.fit.apigateway.crud.dto.AuthenticationRequest;
import br.com.fit.apigateway.crud.dto.ResponseError;
import br.com.fit.apigateway.crud.dto.SuccessAuthentication;
import br.com.fit.apigateway.crud.security.SecurityConstants;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final MessageSource messageSource;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest, HttpServletRequest request) {
        final String DEFAULT_USERNAME = "admin";
        final String DEFAULT_PASSWORD = "admin";

        boolean authenticated = authenticationRequest.getUsername().equalsIgnoreCase(DEFAULT_USERNAME) ||
                authenticationRequest.getPassword().equalsIgnoreCase(DEFAULT_PASSWORD);

        if (!authenticated) {
            List<ObjectError> errors = Collections.singletonList(
                    new ObjectError("authenticationRequest",
                            new String[] { "InvalidCredential" }, null,
                            messageSource.getMessage("invalid_username_or_password", null, request.getLocale())));
            String message = messageSource.getMessage("unauthorized", null, request.getLocale());

            ResponseError errorResponse = new ResponseError(HttpServletResponse.SC_UNAUTHORIZED, errors, message, request.getServletPath());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        String token = JWT.create()
                .withSubject(DEFAULT_USERNAME)
                .withArrayClaim("roles", new String[]{ "ROLE_USER" })
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstants.SECRET_KEY.getBytes()));

        return ResponseEntity.ok(new SuccessAuthentication(token));
    }
}
