package com.esolrpe.client;

import javax.swing.JOptionPane;

public class ExceptionHandler {
    public static void handleException(Throwable e) {
        doExceptionAction(e, false);
    }

    public static void handleRecoverableException(Throwable e) {
        doExceptionAction(e, true);
    }

    private static void doExceptionAction(Throwable e, boolean recoverable) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null,
                "Something went wrong: " + e.getMessage() + "\n" + e.toString(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void setupGlobalExceptionHandling() {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> handleException(e));
    }
}
