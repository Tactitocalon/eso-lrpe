package com.esolrpe.shared.auth;

public interface AccountAPI {
    void authenticate(AuthenticationDetails authenticationDetails);

    void register(AuthenticationDetails authenticationDetails);

    void changePassword(AuthenticationDetails authenticationDetails,
                        String newPassword);
}
