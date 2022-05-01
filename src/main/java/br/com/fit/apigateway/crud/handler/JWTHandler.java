package br.com.fit.apigateway.crud.handler;

import br.com.fit.apigateway.crud.dto.ResponseError;
import br.com.fit.apigateway.crud.security.SecurityConstants;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class JWTHandler {

    private final MessageSource messageSource;

    public JWTHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.HEADER_STRING);

        if (token != null) {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET_KEY.getBytes()))
                    .build()
                    .verify(token);

            String user = decodedJWT.getSubject();

            if (user != null) {
                List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
                List<GrantedAuthority> authorities = new ArrayList<>();

                if (!CollectionUtils.isEmpty(roles)) {
                    roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
                }

                return new UsernamePasswordAuthenticationToken(user, null, authorities);
            }
            return null;
        }
        return null;
    }

    public ResponseError getExpiredTokenResponseBody(HttpServletRequest request) {
        List<ObjectError> errors = Collections.singletonList(new ObjectError("object",
                new String[] { "ExpiredToken" }, null,
                messageSource.getMessage("expired_token", null, request.getLocale())));
        String message = messageSource.getMessage("unauthorized", null, request.getLocale());

        return new ResponseError(HttpStatus.UNAUTHORIZED.value(), errors, message, request.getServletPath());
    }

    public ResponseError getVerificationExceptionBody(HttpServletRequest request) {
        List<ObjectError> errors = Collections.singletonList(new ObjectError("object",
                new String[] { "InvalidToken" }, null,
                messageSource.getMessage("invalid_token", null, request.getLocale())));
        String message = messageSource.getMessage("unauthorized", null, request.getLocale());

        return new ResponseError(HttpStatus.UNAUTHORIZED.value(), errors, message, request.getServletPath());
    }
}
