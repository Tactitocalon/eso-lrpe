package com.esolrpe.client.api;

import com.esolrpe.client.config.Config;
import com.esolrpe.shared.version.VersionAPI;
import com.esolrpe.shared.version.VersionDetails;
import com.google.gson.Gson;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class VersionService implements VersionAPI {
    @Override
    public VersionDetails getCurrentVersion() {
        try {
            HttpUrl url = HttpUrl.parse(Config.getInstance().getServerUri() + "version")
                    .newBuilder()
                    .build();

            Request request = new Request.Builder()
                    .get()
                    .url(url)
                    .build();

            Response response = HttpClient.getInstance()
                    .newCall(request)
                    .execute();

            if (!response.isSuccessful()) {
                throw new RuntimeException("getCurrentVersion failed");
            }

            return new Gson().fromJson(response.body().string(), VersionDetails.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
