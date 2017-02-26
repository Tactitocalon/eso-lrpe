package com.esolrpe.client.config;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Config {
    private static final String CONFIG_FILE_NAME = "lrpe-config.json";

    private static Config config;

    public static Config getInstance() {
        if (config == null) {
            Gson gson = new Gson();
            try {
                String configJson = FileUtils.readFileToString(new File(CONFIG_FILE_NAME));
                config = gson.fromJson(configJson, Config.class);
            } catch (IOException e) {
                config = new Config();
            }
        }

        return config;
    }

    public static void save() {
        Gson gson = new Gson();
        String configJson = gson.toJson(config);

        try {
            FileUtils.writeStringToFile(new File(CONFIG_FILE_NAME), configJson);
        } catch (IOException e) {
            throw new RuntimeException("Could not write configuration to file", e);
        }
    }

    private String serverUri = "https://api.eso-lrpe.com/";

    private String lrpeUsername;
    private String lrpePassword;

    // TODO: We don't need these for the first version of LRPE.
    private boolean isSteam;
    private String esoSteamUri;
    private String esoExecutableLocation;

    private String esoAddonLocation;
    private String lrpeAddonName;

    private String megaserver = "NA";

    public String getServerUri() {
        return serverUri;
    }

    public void setServerUri(String serverUri) {
        this.serverUri = serverUri;
    }

    public String getLrpeUsername() {
        return lrpeUsername;
    }

    public void setLrpeUsername(String lrpeUsername) {
        this.lrpeUsername = lrpeUsername;
    }

    public String getLrpePassword() {
        return lrpePassword;
    }

    public void setLrpePassword(String lrpePassword) {
        this.lrpePassword = lrpePassword;
    }

    public boolean isSteam() {
        return isSteam;
    }

    public void setSteam(boolean steam) {
        isSteam = steam;
    }

    public String getEsoSteamUri() {
        return esoSteamUri;
    }

    public void setEsoSteamUri(String esoSteamUri) {
        this.esoSteamUri = esoSteamUri;
    }

    public String getEsoExecutableLocation() {
        return esoExecutableLocation;
    }

    public void setEsoExecutableLocation(String esoExecutableLocation) {
        this.esoExecutableLocation = esoExecutableLocation;
    }

    public String getEsoAddonLocation() {
        return esoAddonLocation;
    }

    public void setEsoAddonLocation(String esoAddonLocation) {
        this.esoAddonLocation = esoAddonLocation;
    }

    public String getLrpeAddonName() {
        return lrpeAddonName;
    }

    public void setLrpeAddonName(String lrpeAddonName) {
        this.lrpeAddonName = lrpeAddonName;
    }

    public String getMegaserver() {
        return megaserver;
    }

    public void setMegaserver(String megaserver) {
        this.megaserver = megaserver;
    }
}
