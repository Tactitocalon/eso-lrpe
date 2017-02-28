package com.esolrpe.client.tray;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Component;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

public class AppTrayIcon {
    private static AppTrayIcon instance;
    private final CheckboxMenuItem hideToTray;

    private boolean isMinimized = false;
    Set<Component> registeredComponents = Collections.newSetFromMap(new WeakHashMap<>());

    private final PopupMenu popupMenu;
    private final TrayIcon trayIcon;

    private AppTrayIcon() {
        if (!SystemTray.isSupported()) {
            throw new RuntimeException("System tray not supported!");
        }

        Image imgTrayIcon;
        try {
            imgTrayIcon = ImageIO.read(AppTrayIcon.class.getResourceAsStream(
                    "/com/esolrpe/client/icons/icon_resume_color.png"));
            int trayIconWidth = new TrayIcon(imgTrayIcon).getSize().width;
            imgTrayIcon = imgTrayIcon.getScaledInstance(trayIconWidth, -1, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        popupMenu = new PopupMenu();
        trayIcon = new TrayIcon(imgTrayIcon, "eso-lrpe-updater");

        hideToTray = new CheckboxMenuItem("Hide to tray");
        hideToTray.addItemListener(e -> {
            if (hideToTray.getState()) {
                hideApp();
            } else {
                showApp();
            }
        });
        popupMenu.add(hideToTray);
        trayIcon.setPopupMenu(popupMenu);

        final SystemTray tray = SystemTray.getSystemTray();

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            throw new RuntimeException("TrayIcon could not be added.");
        }
    }

    public void registerHideableComponent(Component component) {
        registeredComponents.add(component);
    }

    public void hideApp() {
        registeredComponents.forEach(c -> c.setVisible(false));
        if (!hideToTray.getState()) {
            hideToTray.setState(true);
        }
    }

    public void showApp() {
        registeredComponents.forEach(c -> c.setVisible(true));
        if (hideToTray.getState()) {
            hideToTray.setState(false);
        }
    }

    public static void init() {
        instance = new AppTrayIcon();
    }

    public static AppTrayIcon getInstance() {
        return instance;
    }
}
