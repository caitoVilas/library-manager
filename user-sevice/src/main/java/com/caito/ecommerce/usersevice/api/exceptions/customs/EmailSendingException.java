package com.caito.ecommerce.usersevice.api.exceptions.customs;

public class EmailSendingException extends RuntimeException {
    public EmailSendingException(String message) {
        super(message);
    }
}
