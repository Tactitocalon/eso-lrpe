package com.esolrpe.shared.profiles;

import com.esolrpe.shared.exception.ValidationException;

public class ContextUpdateProfile {
    private String displayName;
    private String profileText;
    private String profileUrl;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfileText() {
        return profileText;
    }

    public void setProfileText(String profileText) {
        this.profileText = profileText;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public void validate() throws ValidationException {
        if (displayName.length() > ProfileAPI.MAXIMUM_DISPLAY_NAME_SIZE) {
            throw new ValidationException("displayName must not be greater than " + ProfileAPI.MAXIMUM_DISPLAY_NAME_SIZE + " characters.");
        }

        if (profileText.length() > ProfileAPI.MAXIMUM_PROFILE_TEXT_SIZE) {
            throw new ValidationException("profileText must not be greater than " + ProfileAPI.MAXIMUM_PROFILE_TEXT_SIZE + " characters.");
        }
    }
}
