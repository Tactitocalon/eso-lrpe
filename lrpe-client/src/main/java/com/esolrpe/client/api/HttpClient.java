package com.esolrpe.client.api;

import com.esolrpe.client.config.Config;
import com.esolrpe.shared.exception.ServiceException;
import com.google.gson.Gson;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

import java.io.IOException;

public class HttpClient {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static OkHttpClient getInstance() {
        return getNewInstance();
    }

    private static OkHttpClient getNewInstance() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.authenticator(new Authenticator() {
            private int attempts = 0;

            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                if (attempts++ > 0) {
                    throw new AuthenticationException();
                }

                String credential = Credentials.basic(Config.getInstance().getLrpeUsername(),
                        Config.getInstance().getLrpePassword());
                return response.request().newBuilder().header("Authorization", credential).build();
            }
        });
        return builder.build();
    }

    public static void resolveExceptionResponse(Response response) throws ServiceException {
        Gson gson = new Gson();
        try {
            ServiceException serviceException = gson.fromJson(response.body().string(), ServiceException.class);
            if (serviceException.getException() != null && !serviceException.getException().isEmpty()) {
                throw serviceException;
            }
            throw new RuntimeException("An unknown error occurred: " + response.body().string());
        } catch (IOException e) {
            throw new RuntimeException("Error in resolving exception response", e);
        }
    }
}
