package com.esolrpe.client.api;

import com.esolrpe.client.config.Config;
import com.esolrpe.shared.auth.AccountAPI;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public class AccountService implements AccountAPI {
    private static final AccountService INSTANCE = new AccountService();
    public static AccountService getInstance() {
        return INSTANCE;
    }

    @Override
    public void authenticate() {
        Request request = new Request.Builder()
                .url(Config.getInstance().getServerUri() + "accounts/authenticate")
                .build();
        try {
            Response response = HttpClient.getInstance()
                    .newCall(request)
                    .execute();

            if (!response.isSuccessful()) {
                throw new RuntimeException("authenticate failed");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void register(String username, String password) {
        HttpUrl url = HttpUrl.parse(Config.getInstance().getServerUri())
                .newBuilder()
                .addEncodedPathSegment("accounts")
                .addEncodedPathSegment("register")
                .build();

        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .post(formBody)
                .url(url)
                .build();

        Response response;
        try {
            response = HttpClient.getInstance()
                    .newCall(request)
                    .execute();

            if (!response.isSuccessful()) {
                throw new RuntimeException("register failed");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changePassword(String newPassword) {

    }
}
