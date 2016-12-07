package com.esolrpe.profiles;

import java.util.List;

public class ProfileDatabaseUpdate {
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
}
