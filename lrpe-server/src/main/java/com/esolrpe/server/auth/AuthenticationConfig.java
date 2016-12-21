package com.esolrpe.server.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class AuthenticationConfig extends WebSecurityConfigurerAdapter {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthenticationConfig(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(jdbcTemplate.getDataSource())
                .passwordEncoder(getPasswordEncoder())
                .usersByUsernameQuery("SELECT username, password, isEnabled FROM accounts WHERE username=?")
                .authoritiesByUsernameQuery(
                        "SELECT username, " +
                        "CASE WHEN isAdmin = 1 THEN 'ADMIN' ELSE 'USER' END AS authority " +
                        "FROM accounts WHERE username=?");
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/accounts/register").anonymous();
        http.authorizeRequests().anyRequest().fullyAuthenticated();
        http.httpBasic();
        http.csrf().disable();
    }

    public static PasswordEncoder getPasswordEncoder() {
        return new SCryptPasswordEncoder();
    }
}
