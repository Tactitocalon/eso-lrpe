package com.esolrpe.shared.version;

public class VersionDetails {
    public static VersionDetails createCurrentVersionDetails() {
        VersionDetails versionDetails = new VersionDetails();
        versionDetails.version = 2;
        versionDetails.updateMessage = "A new version of LRPE is available. " +
                "Please download the most recent update: https://github.com/Tactitocalon/eso-lrpe";
        versionDetails.updateUrl = "https://github.com/Tactiticalon/eso-lrpe";
        return versionDetails;
    }

    private int version;
    private String updateMessage;
    private String updateUrl;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getUpdateMessage() {
        return updateMessage;
    }

    public void setUpdateMessage(String updateMessage) {
        this.updateMessage = updateMessage;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }
}
