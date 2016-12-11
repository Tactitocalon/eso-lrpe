    package com.esolrpe.client;

    import com.esolrpe.client.config.Config;
    import com.esolrpe.client.forms.ConfigureAddonLocationForm;
    import com.esolrpe.client.forms.MainForm;

    import javax.swing.SwingUtilities;

    public class Application {
        public static final String TITLE = "LRPE Launcher";

        public static void main(String[] args) {
            run();
        }

        public static void run() {
            assert(!SwingUtilities.isEventDispatchThread());
            try {
                Config config = Config.getInstance();

                ConfigureAddonLocationForm.display();

                MainForm.display();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
