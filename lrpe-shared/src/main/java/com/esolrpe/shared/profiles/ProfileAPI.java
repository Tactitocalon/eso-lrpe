package com.esolrpe.shared.profiles;

import java.util.List;

public interface ProfileAPI {
    ProfileDatabaseUpdate syncProfiles(
            String megaserverCode,
            Long currentVersion);

    void updateProfile(String megaserverCode,
                       String characterName,
                       ContextUpdateProfile profileData);

    void deleteProfile(String megaserverCode, ContextDeleteProfile profileData);

    List<ProfileData> getProfilesForAccount(String megaserverCode);
}
