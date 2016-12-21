package com.esolrpe.server.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthenticationUtils {
    public static UserDetails getUserDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) auth.getPrincipal();
    }

    public static String encodePlaintextPassword(String password) {
        PasswordEncoder encoder = AuthenticationConfig.getPasswordEncoder();
        return encoder.encode(password);
    }
}
