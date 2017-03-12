package com.esolrpe.client.startup;

import com.esolrpe.client.Application;
import com.esolrpe.client.config.Config;
import mslinks.ShellLink;
import org.apache.commons.io.FileUtils;

import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;

// TODO: this is Windows only ATM. need to make platform independent, or at least fail gracefully on non-Windows platforms.
public class StartupLaunchUtils {
    public static void askStartupLaunchQuestion() {
        Config config = Config.getInstance();
        if (config.isAskedStartupLaunchQuestion()) {
            return;
        }

        int result = JOptionPane.showConfirmDialog(null, "Automatically update profiles on Windows startup?", Application.TITLE, JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            installStartupLaunch();
        }

        config.setAskedStartupLaunchQuestion(true);
        Config.save();
    }

    public static boolean checkIfStartupLaunchInstalled() {
        String startupFolderStr = VBSUtils.getSpecialFolder(VBSUtils.SF_STARTUP);
        File startupLink = new File(startupFolderStr, "eso-lrpe-updater.lnk");
        return startupLink.isFile();
    }

    public static void uninstallStartupLaunch() {
        String startupFolderStr = VBSUtils.getSpecialFolder(VBSUtils.SF_STARTUP);
        File startupLink = new File(startupFolderStr, "eso-lrpe-updater.lnk");
        FileUtils.deleteQuietly(startupLink);
    }

    public static void installStartupLaunch() {
        String startupFolderStr = VBSUtils.getSpecialFolder(VBSUtils.SF_STARTUP);
        File startupLink = new File(startupFolderStr, "eso-lrpe-updater.lnk");

        String currentDirectory = System.getProperty("user.dir");

        ShellLink shellLink = new ShellLink();
        shellLink.setWorkingDir(currentDirectory);
        shellLink.setTarget(new File(currentDirectory, "eso-lrpe-updater.jar").getAbsolutePath());
        shellLink.setCMDArgs("-startup");

        try {
            shellLink.saveTo(startupLink.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
