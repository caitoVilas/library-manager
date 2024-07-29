package com.caito.ecommerce.usersevice.api.exceptions.handlers;

import com.caito.ecommerce.usersevice.api.exceptions.customs.ValidateRegistrationException;
import com.caito.ecommerce.usersevice.api.models.responses.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ValidateRegistrationExceptionHandler {

    @ExceptionHandler(ValidateRegistrationException.class)
    protected ResponseEntity<ExceptionResponse> validateRegistrationHandler(ValidateRegistrationException e,
                                                                            HttpServletRequest request){
        var response = ExceptionResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .method(request.getMethod())
                .path(request.getRequestURL().toString())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
