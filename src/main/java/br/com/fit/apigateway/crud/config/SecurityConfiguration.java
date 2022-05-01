package br.com.fit.apigateway.crud.config;

import br.com.fit.apigateway.crud.filter.JWTAuthorizationFilter;
import br.com.fit.apigateway.crud.handler.JWTHandler;
import br.com.fit.apigateway.crud.security.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final JWTHandler jwtHandler;
	private final MessageSource messageSource;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.httpBasic()
					.disable()
				.csrf()
					.disable()
				.headers()
					.frameOptions()
					.disable()
				.and()
					.cors()
				.and()
				.exceptionHandling()
					.authenticationEntryPoint(authenticationEntryPoint())
				.and()
					.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/login").permitAll()
				.antMatchers("/h2-console/**").permitAll()
				.antMatchers(HttpMethod.GET, "/api-docs/**").permitAll()
				.antMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
				.antMatchers(HttpMethod.GET, "/swagger-ui/index.html**").permitAll()
				.antMatchers("/clientes**").hasRole("USER")
				.anyRequest().authenticated()
				.and()
				.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtHandler));
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint(){
		return new CustomAuthenticationEntryPoint(messageSource);
	}

	@Bean
	public CorsFilter corsFilter() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		final CorsConfiguration config = new CorsConfiguration();

		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("OPTIONS");
		config.addExposedHeader("Content-Disposition");
		config.addAllowedMethod("HEAD");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("DELETE");
		config.addAllowedMethod("PATCH");
		source.registerCorsConfiguration("/**", config);

		return new CorsFilter(source);
	}
}
