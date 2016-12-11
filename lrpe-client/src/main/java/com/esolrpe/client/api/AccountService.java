package com.esolrpe.client.api;

import com.esolrpe.shared.auth.AccountAPI;
import com.esolrpe.shared.auth.AuthenticationDetails;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class AccountService implements AccountAPI {
    @Override
    public void authenticate(AuthenticationDetails authenticationDetails) {

    }

    @Override
    public void register(AuthenticationDetails authenticationDetails) {

    }

    @Override
    public void changePassword(AuthenticationDetails authenticationDetails, String newPassword) {

    }
}
