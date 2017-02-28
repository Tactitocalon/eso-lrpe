package com.esolrpe.client.forms;

import com.esolrpe.client.tray.AppTrayIcon;

import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class FormUtils {
    public static void centerWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    public static void setIcon(Window window) {
        try {
            BufferedImage imgIcon = ImageIO.read(AppTrayIcon.class.getResourceAsStream(
                    "/com/esolrpe/client/icons/icon_resume_color.png"));
            window.setIconImage(imgIcon);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
