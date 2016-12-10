package com.esolrpe.shared.profiles;

import com.esolrpe.shared.auth.AuthenticationDetails;

public interface ProfileAPI {
    ProfileDatabaseUpdate syncProfiles(
            String megaserverCode,
            Long currentVersion);

    void updateProfile(String megaserverCode,
                       String characterName,
                       ContextUpdateProfile profileData);

    void deleteProfile(String megaserverCode, ContextDeleteProfile profileData);

    void getProfilesForAccount(String megaserverCode, AuthenticationDetails authenticationDetails);
}
