package com.esolrpe.auth;

import org.springframework.jdbc.core.JdbcTemplate;

public class AuthenticationDetails {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void authenticate(JdbcTemplate jdbcTemplate) throws AuthenticationException {

    }

    public static class AuthenticationException extends RuntimeException {

    }
}
