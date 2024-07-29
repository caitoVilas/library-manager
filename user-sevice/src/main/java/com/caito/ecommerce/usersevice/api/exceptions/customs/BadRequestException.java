package com.caito.ecommerce.usersevice.api.exceptions.customs;

import lombok.Getter;

import java.util.List;

@Getter
public class BadRequestException extends RuntimeException{
    private List<String> messages;
    public BadRequestException(List<String> messages){
        super();
        this.messages = messages;
    }
}
