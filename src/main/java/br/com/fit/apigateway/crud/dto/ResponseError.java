package br.com.fit.apigateway.crud.dto;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Getter
@ToString
public class ResponseError {

    private final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    private final int status;
    private final String error;
    private final Collection<?> errors;
    private final String message;
    private final String path;

    public ResponseError(int status, Collection<?> errors, String message, String path) {
        this.status = status;
        this.error = getError(status);
        this.errors = errors;
        this.message = message;
        this.path = path;
    }

    private String getError(int status) {
        switch (status) {
            case 400:
                return "Bad Request";
            case 401:
                return "Unauthorized";
            default:
                return "Error";
        }
    }
}
