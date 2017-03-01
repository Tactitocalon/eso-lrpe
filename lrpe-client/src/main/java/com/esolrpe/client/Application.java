    package com.esolrpe.client;

    import com.esolrpe.client.api.AccountService;
    import com.esolrpe.client.api.AuthenticationException;
    import com.esolrpe.client.api.VersionService;
    import com.esolrpe.client.config.Config;
    import com.esolrpe.client.forms.ConfigureAddonLocationForm;
    import com.esolrpe.client.forms.ConfigureCredentialsForm;
    import com.esolrpe.client.forms.MainForm;
    import com.esolrpe.client.startup.StartupLaunchUtils;
    import com.esolrpe.client.tray.AppTrayIcon;
    import com.esolrpe.shared.version.VersionDetails;

    import javax.swing.SwingUtilities;

    public class Application {
        public static final String TITLE = "eso-lrpe-updater";
        public static boolean STARTUP_MODE = false;

        public static void main(String[] args) throws InterruptedException {
            if (args.length >= 1 && "-startup".equals(args[0])) {
                STARTUP_MODE = true;
                Thread.sleep(10000);
            }
            run();
        }

        private static void run() {
            try {
                assert(!SwingUtilities.isEventDispatchThread());
                ExceptionHandler.setupGlobalExceptionHandling();
                Config config = Config.getInstance();

                AppTrayIcon.init();
                if (STARTUP_MODE) {
                    AppTrayIcon.getInstance().hideApp();
                }

                doVersionCheck();
                if (config.getEsoAddonLocation() == null) {
                    ConfigureAddonLocationForm.display();
                }

                // Attempt authentication
                boolean isAuthenticated = false;
                try {
                    new AccountService().authenticate();
                    isAuthenticated = true;
                } catch (AuthenticationException ignored) {}
                if (!isAuthenticated) {
                    ConfigureCredentialsForm.display();
                }

                MainForm.display();
            } catch (Exception e) {
                ExceptionHandler.handleException(e);
                System.exit(0);
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
