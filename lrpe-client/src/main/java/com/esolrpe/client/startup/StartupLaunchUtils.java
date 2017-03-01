package com.esolrpe.client.startup;

import mslinks.ShellLink;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

// TODO: this is Windows only ATM. need to make platform independent, or at least fail gracefully on non-Windows platforms.
public class StartupLaunchUtils {
    public static void askStartupLaunchQuestion() {

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
