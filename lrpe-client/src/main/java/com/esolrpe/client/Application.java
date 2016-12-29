    package com.esolrpe.client;

    import com.esolrpe.client.api.VersionService;
    import com.esolrpe.client.config.Config;
    import com.esolrpe.client.forms.ConfigureAddonLocationForm;
    import com.esolrpe.client.forms.MainForm;
    import com.esolrpe.shared.version.VersionDetails;

    import javax.swing.SwingUtilities;

    public class Application {
        public static final String TITLE = "LRPE Launcher";

        public static void main(String[] args) {
            run();
        }

        private static void run() {
            try {
                assert(!SwingUtilities.isEventDispatchThread());
                ExceptionHandler.setupGlobalExceptionHandling();
                Config config = Config.getInstance();

                doVersionCheck();
                ConfigureAddonLocationForm.display();

                MainForm.display();
            } catch (Exception e) {
                ExceptionHandler.handleException(e);
            }
        }

        private static void doVersionCheck() {
            VersionDetails serverVersion = new VersionService().getCurrentVersion();
            VersionDetails currentVersion = VersionDetails.createCurrentVersionDetails();

            if (serverVersion.getVersion() > currentVersion.getVersion()) {
                throw new RuntimeException(serverVersion.getUpdateMessage());
            }
        }
    }
