package com.esolrpe.server.version;

import com.esolrpe.shared.version.VersionAPI;
import com.esolrpe.shared.version.VersionDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class VersionController implements VersionAPI {
    @Override
    @RequestMapping(value="version", method = RequestMethod.GET)
    public VersionDetails getCurrentVersion() {
        return VersionDetails.createCurrentVersionDetails();
    }
}
