package com.esolrpe.profiles;

import com.esolrpe.Application;
import com.esolrpe.auth.AuthenticationDetails;
import com.esolrpe.util.Megaserver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProfileController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @RequestMapping(value="{megaserverCode}/sync", method = RequestMethod.GET)
    public ProfileDatabaseUpdate syncProfiles(
            @PathVariable String megaserverCode,
            @RequestParam(defaultValue = "0") Long currentVersion) {
        Megaserver megaserver = Megaserver.fromCode(StringUtils.upperCase(megaserverCode));
        if (megaserver == null) {
            throw new RuntimeException("Unknown megaserver '" + megaserverCode + "'.");
        }

        Timestamp currentVersionTimestamp = new Timestamp(currentVersion);

        ProfileDatabaseUpdate databaseUpdate = new ProfileDatabaseUpdate();
        databaseUpdate.setApplicationVersion(Application.APPLICATION_VERSION);

        Optional<Timestamp> timestamp = Optional.ofNullable(jdbcTemplate.queryForObject(
                "SELECT MAX(profiles.lastUpdate) FROM profiles WHERE profiles.megaserver = ?",
                new Object[] { megaserver.getDatabaseCode() },
                Timestamp.class
        ));
        if (timestamp.isPresent()) {
            databaseUpdate.setDatabaseVersion(timestamp.get().getTime());
        } else {
            // If there are no profiles available on this server.
            databaseUpdate.setDatabaseVersion(0L);
        }

        int profileCount = jdbcTemplate.queryForObject(
                "SELECT count(1) FROM profiles WHERE profiles.megaserver = ? AND deleted = ?",
                new Object[] { megaserver.getDatabaseCode(), false },
                Integer.class
        );
        databaseUpdate.setTotalProfileCount(profileCount);

        List<ProfileData> profileData = jdbcTemplate.query(
                "SELECT * FROM profiles WHERE profiles.megaserver = ? AND lastUpdate >= ?",
                new Object[] { megaserver.getDatabaseCode(), currentVersionTimestamp },
                new BeanPropertyRowMapper<>(ProfileData.class)
        );
        databaseUpdate.setProfiles(profileData);

        return databaseUpdate;
    }

    @RequestMapping(value = "{megaserverCode}/{characterName}", method = RequestMethod.PUT)
    public void updateProfile(@PathVariable String megaserverCode,
                              @PathVariable String characterName,
                              @RequestBody ContextUpdateProfile profileData) {
        profileData.getAuthenticationDetails().authenticate(jdbcTemplate);

        // TODO update
    }

    @RequestMapping(value = "{megaserverCode}/{characterName}/delete", method = RequestMethod.POST)
    public void deleteProfile(@PathVariable String megaserverCode,
                              @RequestBody ContextDeleteProfile profileData) {
        profileData.getAuthenticationDetails().authenticate(jdbcTemplate);

        // TODO delete
    }

    @RequestMapping(value = "{megaserverCode}/accountprofiles", method = RequestMethod.GET)
    public void getProfilesForAccount(@PathVariable String megaserverCode,
                                      @RequestBody AuthenticationDetails authenticationDetails) {
        authenticationDetails.authenticate(jdbcTemplate);

        // TODO get profiles for account
    }
}
