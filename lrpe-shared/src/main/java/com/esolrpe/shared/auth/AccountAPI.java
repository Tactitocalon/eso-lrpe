package com.esolrpe.shared.auth;

public interface AccountAPI {
    void authenticate();

    void register(String username, String password);

    void changePassword(String newPassword);
}
