package com.esolrpe.profiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/profiles")
public class ProfilesController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProfilesController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @RequestMapping(value="{megaserver}/getDatabaseUpdate", method = RequestMethod.GET)
    ProfileDatabaseUpdate getDatabaseUpdate(
            @PathVariable int megaserver,
            @RequestParam(defaultValue = "0") String currentVersion) {
        ProfileDatabaseUpdate databaseUpdate = new ProfileDatabaseUpdate();
        databaseUpdate.setApplicationVersion(1);
        databaseUpdate.setDatabaseVersion(1337L);

        int profileCount = jdbcTemplate.queryForObject(
                "SELECT count(1) FROM profiles WHERE profiles.megaserver = ?",
                new Object[] { megaserver },
                Integer.class
        );
        databaseUpdate.setTotalProfileCount(profileCount);

        List<ProfileData> profileData = jdbcTemplate.query(
                "SELECT * FROM profiles WHERE profiles.megaserver = ?",
                new Object[] { megaserver },
                new BeanPropertyRowMapper<>(ProfileData.class)
        );
        databaseUpdate.setProfiles(profileData);

        return databaseUpdate;
    }



}
