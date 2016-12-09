package com.esolrpe.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AccountController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @RequestMapping(value="authenticate", method = RequestMethod.GET)
    public void authenticate(@RequestParam AuthenticationDetails authenticationDetails) {
        authenticationDetails.authenticate(jdbcTemplate);
    }

    @RequestMapping(value="register", method = RequestMethod.POST)
    public void register(@RequestParam AuthenticationDetails authenticationDetails) {

    }

    @RequestMapping(value = "changepassword", method = RequestMethod.PUT)
    public void changePassword(@RequestParam AuthenticationDetails authenticationDetails,
                               @RequestParam String newPassword) {
        authenticationDetails.authenticate(jdbcTemplate);

        // TODO update
    }
}
