package com.esolrpe.client.api;

import okhttp3.OkHttpClient;

public class HttpClient {
    public static final OkHttpClient client = new OkHttpClient();

    public static OkHttpClient getInstance() {
        return client;
    }
}
