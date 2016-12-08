package com.esolrpe.profiles;

import com.esolrpe.auth.AuthenticationDetails;

public class ContextDeleteProfile {
    private AuthenticationDetails authenticationDetails;
    private String characterName;

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public AuthenticationDetails getAuthenticationDetails() {
        return authenticationDetails;
    }

    public void setAuthenticationDetails(AuthenticationDetails authenticationDetails) {
        this.authenticationDetails = authenticationDetails;
    }
}
