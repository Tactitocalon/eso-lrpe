package com.esolrpe.server.auth;

import com.esolrpe.shared.auth.AccountAPI;
import com.esolrpe.shared.auth.AccountAlreadyExistsException;
import com.esolrpe.shared.exception.ServiceException;
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
    public void authenticate() {}

    @Override
    @RequestMapping(value="register", method = RequestMethod.POST)
    public void register(@RequestParam String username, @RequestParam String password) {
        int existingAccount = jdbcTemplate.queryForObject(
                "SELECT count(1) FROM accounts WHERE username = ?",
                new Object[] { username },
                Integer.class
        );
        if (existingAccount != 0) {
            throw new AccountAlreadyExistsException("An account with the username \"" + username + "\" already exists.");
        }

        String encodedPassword = AuthenticationUtils.encodePlaintextPassword(password);
        jdbcTemplate.update("INSERT INTO accounts (username, password) VALUES (?, ?)",
                new Object[] {username, encodedPassword}
        );
    }

    @Override
    @RequestMapping(value = "changepassword", method = RequestMethod.PUT)
    public void changePassword(@RequestParam String newPassword) {
        // AuthenticationUtils.authenticate(authenticationDetails, jdbcTemplate);

        // TODO update
    }
}
