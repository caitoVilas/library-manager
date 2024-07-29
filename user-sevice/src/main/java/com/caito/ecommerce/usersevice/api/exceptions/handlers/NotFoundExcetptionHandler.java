package com.caito.ecommerce.usersevice.api.exceptions.handlers;

import com.caito.ecommerce.usersevice.api.exceptions.customs.NotFoundException;
import com.caito.ecommerce.usersevice.api.models.responses.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundExcetptionHandler {

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ExceptionResponse> notFoundHandler(NotFoundException e, HttpServletRequest request){
        var response = ExceptionResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .status(HttpStatus.NOT_FOUND.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .method(request.getMethod())
                .path(request.getRequestURL().toString())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
