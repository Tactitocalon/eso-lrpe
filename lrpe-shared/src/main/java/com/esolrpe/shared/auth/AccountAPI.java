package com.esolrpe.shared.auth;

import com.esolrpe.shared.exception.ServiceException;

public interface AccountAPI {
    void authenticate();

    void register(String username, String password);

    void changePassword(String newPassword);
}
