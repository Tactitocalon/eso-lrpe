package com.esolrpe.profiles;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profiles")
public class ProfilesController {

    @RequestMapping(value="getDatabaseUpdate", method = RequestMethod.GET)
    ProfileDatabaseUpdate getDatabaseUpdate(@RequestParam(defaultValue = "0") String currentVersion) {
        ProfileDatabaseUpdate databaseUpdate = new ProfileDatabaseUpdate();
        databaseUpdate.setApplicationVersion(1);
        databaseUpdate.setDatabaseVersion(1337L);

        return databaseUpdate;
    }



}
