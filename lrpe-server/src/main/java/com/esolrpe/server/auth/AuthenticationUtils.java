package com.esolrpe.server.auth;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class AuthenticationUtils {
    public static UserDetails getUserDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) auth.getPrincipal();
    }

    public static String encodePlaintextPassword(String password) {
        PasswordEncoder encoder = AuthenticationConfig.getPasswordEncoder();
        return encoder.encode(password);
    }

    public static long getCurrentAccountId(JdbcTemplate jdbcTemplate) {
        UserDetails userDetails = getUserDetails();
        String username = userDetails.getUsername();

        return Optional.ofNullable(jdbcTemplate.queryForObject(
                "SELECT accountId FROM accounts WHERE username = ?",
                new Object[] { username },
                Long.class
        )).orElse(0L);
    }
}
