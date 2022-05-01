package br.com.fit.apigateway.crud.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class SuccessAuthentication {

    private final String token;
    private final String type = "Bearer";
}
