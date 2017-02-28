package com.esolrpe.client.profiles;

import com.esolrpe.client.api.ProfileService;
import com.esolrpe.client.config.Config;
import com.esolrpe.client.forms.FormUtils;
import com.esolrpe.shared.profiles.ProfileDatabase;
import com.esolrpe.shared.profiles.ProfileDatabaseUpdate;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class ProfileManager {
    public static boolean updateProfiles(Consumer<String> outputAppender) {
        outputAppender.accept(FormUtils.getNowAsString() + " - Beginning profile database update.\n");

        ProfileDatabaseUpdate profileDatabaseUpdate;
        try {
            profileDatabaseUpdate = new ProfileService().syncProfiles(Config.getInstance().getMegaserver(), 0L);
        } catch (Exception exception) {
            outputAppender.accept(FormUtils.getNowAsString() + " - Error occurred profile database update.\n");
            outputAppender.accept(FormUtils.getStackTrace(exception) + "\n");
            throw exception;
        }

        // TODO: need to read an existing profile database or w/e
        ProfileDatabase profileDatabase = new ProfileDatabase();
        profileDatabase.setApplicationVersion(profileDatabaseUpdate.getApplicationVersion());
        profileDatabase.setDatabaseVersion(profileDatabaseUpdate.getDatabaseVersion());
        profileDatabase.setTotalProfileCount(profileDatabaseUpdate.getTotalProfileCount());
        profileDatabase.setProfiles(profileDatabaseUpdate.getProfiles());

        File database = new File(Config.getInstance().getEsoAddonLocation(), "LitheRPEssentials/data/LitheDatabase.lua");
        database.delete();

        try {
            profileDatabase.exportToLuaDatabase(database);
        } catch (IOException e1) {
            throw new RuntimeException(e1);
        }

        int newProfiles = profileDatabaseUpdate.getProfiles().size();
        int totalProfiles = profileDatabase.getProfiles().size();
        outputAppender.accept(FormUtils.getNowAsString() + " - Database update complete.\nReceived " + newProfiles
                + " new profile" + (newProfiles == 1 ? "" : "s")
                + ", database now contains " + totalProfiles + " profiles.\n");

        return true;
    }
}
