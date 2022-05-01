package br.com.fit.apigateway.crud.security;

import br.com.fit.apigateway.crud.dto.ResponseError;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final MessageSource messageSource;
	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
			List<ObjectError> errors = Collections.singletonList(new ObjectError("object",
					new String[] { "Unauthorized" }, null,
					messageSource.getMessage("unauthorized_access", null, request.getLocale())));
			String message = messageSource.getMessage("unauthorized", null, request.getLocale());

			int status = HttpServletResponse.SC_UNAUTHORIZED;
			String path = request.getServletPath();
			ResponseError responseError = new ResponseError(status, errors, message, path);

			response.setContentType("application/json;charset=UTF-8");
			response.setStatus(status);
			response.getWriter().write(objectMapper.writeValueAsString(responseError));
	}
}
