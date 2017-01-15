package com.esolrpe.client.api;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super("Invalid username / password combination.");
    }
}
