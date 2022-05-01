package br.com.fit.apigateway.crud.filter;

import br.com.fit.apigateway.crud.handler.JWTHandler;
import br.com.fit.apigateway.crud.security.SecurityConstants;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private final JWTHandler jwtHandler;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTHandler jwtHandler) {
		super(authenticationManager);
		this.jwtHandler = jwtHandler;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		String header = request.getHeader(SecurityConstants.HEADER_STRING);

        if (header == null) {
            chain.doFilter(request, response);
            return;
        }

        try {
        	SecurityContextHolder.getContext().setAuthentication(jwtHandler.getAuthentication(request));
        } catch (TokenExpiredException e) {
			response.getWriter().write(objectMapper.writeValueAsString(jwtHandler.getExpiredTokenResponseBody(request)));
        } catch (JWTVerificationException e) {
			response.getWriter().write(objectMapper.writeValueAsString(jwtHandler.getVerificationExceptionBody(request)));
        }
        
        chain.doFilter(request, response);
	}
}
