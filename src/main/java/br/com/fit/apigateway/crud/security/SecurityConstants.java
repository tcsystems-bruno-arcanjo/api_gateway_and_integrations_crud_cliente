package br.com.fit.apigateway.crud.security;

public class SecurityConstants {
	
    public static final String SECRET_KEY = "fitimpacta@2022";
    public static final long EXPIRATION_TIME = 864000000; // 10 days
    public static final String HEADER_STRING = "X-Authorization";
}
