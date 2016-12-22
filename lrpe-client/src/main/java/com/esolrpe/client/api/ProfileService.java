package com.esolrpe.client.api;

import com.esolrpe.client.config.Config;
import com.esolrpe.shared.profiles.ContextDeleteProfile;
import com.esolrpe.shared.profiles.ContextUpdateProfile;
import com.esolrpe.shared.profiles.ProfileAPI;
import com.esolrpe.shared.profiles.ProfileData;
import com.esolrpe.shared.profiles.ProfileDatabaseUpdate;
import com.google.gson.Gson;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ProfileService implements ProfileAPI {
    @Override
    public ProfileDatabaseUpdate syncProfiles(String megaserverCode, Long currentVersion) {
        Request request = new Request.Builder()
                .url(Config.getInstance().getServerUri() + "profiles/na/sync")
                .build();
        try {
            Response response = HttpClient.getInstance()
                    .newCall(request)
                    .execute();

            if (!response.isSuccessful()) {
                throw new RuntimeException("syncProfiles failed");
            }

            return new Gson().fromJson(response.body().string(), ProfileDatabaseUpdate.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateProfile(String megaserverCode, String characterName, ContextUpdateProfile profileData) {
        HttpUrl url = HttpUrl.parse(Config.getInstance().getServerUri())
                .newBuilder()
                .addEncodedPathSegment("profiles")
                .addEncodedPathSegment(Config.getInstance().getMegaserver())
                .addEncodedPathSegment(characterName)
                .build();

        Request request = new Request.Builder()
                .put(RequestBody.create(HttpClient.JSON, new Gson().toJson(profileData)))
                .url(url)
                .build();

        Response response = null;
        try {
            response = HttpClient.getInstance()
                    .newCall(request)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!response.isSuccessful()) {
            throw new RuntimeException("updateProfile failed");
        }
    }

    @Override
    public void deleteProfile(String megaserverCode, ContextDeleteProfile profileData) {

    }

    @Override
    public List<ProfileData> getProfilesForAccount(String megaserverCode) {
        try {
            HttpUrl url = HttpUrl.parse(Config.getInstance().getServerUri() + "profiles/na/accountprofiles")
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
                throw new RuntimeException("getProfilesForAccount failed");
            }

            return Arrays.asList(new Gson().fromJson(response.body().string(), ProfileData[].class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
