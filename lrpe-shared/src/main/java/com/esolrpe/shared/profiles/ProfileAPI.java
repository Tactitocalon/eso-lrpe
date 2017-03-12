package com.esolrpe.shared.profiles;

import java.util.List;

public interface ProfileAPI {
    int MAXIMUM_ACTUAL_NAME_SIZE = 25;
    int MAXIMUM_DISPLAY_NAME_SIZE = 50;
    int MAXIMUM_PROFILE_TEXT_SIZE = 2000;

    ProfileDatabaseUpdate syncProfiles(
            String megaserverCode,
            Long currentVersion);

    void updateProfile(String megaserverCode,
                       String characterName,
                       ContextUpdateProfile profileData);

    void deleteProfile(String megaserverCode, ContextDeleteProfile profileData);

    List<ProfileData> getProfilesForAccount(String megaserverCode);
}
