package com.esolrpe.server.auth;

import com.esolrpe.shared.auth.AccountAPI;
import com.esolrpe.shared.auth.AuthenticationDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController implements AccountAPI {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AccountController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @RequestMapping(value="authenticate", method = RequestMethod.GET)
    public void authenticate(@RequestParam AuthenticationDetails authenticationDetails) {
        // AuthenticationUtils.authenticate(authenticationDetails, jdbcTemplate);
    }

    @Override
    @RequestMapping(value="register", method = RequestMethod.POST)
    public void register(@RequestParam AuthenticationDetails authenticationDetails) {

    }

    @Override
    @RequestMapping(value = "changepassword", method = RequestMethod.PUT)
    public void changePassword(@RequestParam AuthenticationDetails authenticationDetails,
                               @RequestParam String newPassword) {
        // AuthenticationUtils.authenticate(authenticationDetails, jdbcTemplate);

        // TODO update
    }
}
