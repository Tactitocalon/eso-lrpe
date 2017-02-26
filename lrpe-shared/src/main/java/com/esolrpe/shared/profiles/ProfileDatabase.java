package com.esolrpe.shared.profiles;

import com.esolrpe.shared.security.Security;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ProfileDatabase {
    private Long databaseVersion;
    private int applicationVersion;
    private int totalProfileCount;

    private List<ProfileData> profiles;

    public Long getDatabaseVersion() {
        return databaseVersion;
    }

    public void setDatabaseVersion(Long databaseVersion) {
        this.databaseVersion = databaseVersion;
    }

    public int getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(int applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public int getTotalProfileCount() {
        return totalProfileCount;
    }

    public void setTotalProfileCount(int totalProfileCount) {
        this.totalProfileCount = totalProfileCount;
    }

    public List<ProfileData> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<ProfileData> profiles) {
        this.profiles = profiles;
    }

    public void exportToLuaDatabase(File file) throws IOException {
        FileWriter writer = new FileWriter(file, false);

        writer.write("LitheDatabase = {");
        // ESO GetTimeStamp() returns seconds, not milliseconds
        writer.write("lastUpdateTimestamp = " + (System.currentTimeMillis() / 1000L) + ",");
        writer.write("applicationVersion = " + applicationVersion + ",");
        writer.write("databaseVersion = " + databaseVersion + ",");
        writer.write("totalProfileCount = " + totalProfileCount + ",");

        boolean firstProfile = true;
        for (ProfileData profile : profiles) {
            if (!firstProfile) {
                writer.write(",");
            }
            writer.write("[\"" + profile.getCharacterName() + "\"]={nm=\""
                    + Security.escapeLuaString(profile.getDisplayName()) + "\",pf=\""
                    + Security.escapeLuaString(profile.getProfileText()) + "\",url=\""
                    + Security.escapeLuaString(profile.getProfileUrl()) + "\"}");
            firstProfile = false;
        }

        writer.write("}");

        writer.close();
    }
}
